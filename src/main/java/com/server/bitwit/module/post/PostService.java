package com.server.bitwit.module.post;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.PostLike;
import com.server.bitwit.domain.Tag;
import com.server.bitwit.module.error.exception.InvalidRequestException;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.dto.*;
import com.server.bitwit.module.post.search.PostSearchCond;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import com.server.bitwit.module.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    
    private final PostRepository     postRepository;
    private final PostLikeRepository likeRepository;
    private final StockRepository    stockRepository;
    private final TagRepository      tagRepository;
    
    private final CommentService    commentService;
    private final ConversionService conversionService;
    
    public PostResponse createPost(CreatePostRequest request) {
        return Optional.ofNullable(conversionService.convert(request, Post.class))
                       .map(postRepository::save)
                       .map(post -> conversionService.convert(post, PostResponse.class))
                       .orElseThrow( );
    }
    
    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request) {
        var inputStream         = new ByteArrayInputStream(new byte[] {1, 2, 3});
        var bufferedInputStream = new BufferedInputStream(inputStream);
        return postRepository.findById(postId)
                             .map(post -> {
                                 if (StringUtils.hasText(request.getTitle( ))) {
                                     post.updateTitle(request.getTitle( ));
                                 }
                                 if (StringUtils.hasText(request.getContent( ))) {
                                     post.updateContent(request.getContent( ));
                                 }
                                 if (! CollectionUtils.isEmpty(request.getTickers( ))) {
                                     post.clearTag( );
                                     request.getTags( )
                                            .stream( )
                                            .map(name ->
                                                    tagRepository.findByName(name)
                                                                 .orElseGet(( ) -> tagRepository.save(new Tag(name)))
                                            )
                                            .forEach(post::addTag);
                                     var cond = new SearchStockCond( );
                                     cond.setTickers(request.getTickers( ));
                                     var stocks = stockRepository.searchStocks(cond);
                                     post.updateStocks(new HashSet<>(stocks));
                                 }
                                 return conversionService.convert(post, PostResponse.class);
                             })
                             .orElseThrow(( ) -> new NonExistentResourceException("post", postId));
    }
    
    @Transactional
    public List<PostResponse> searchPosts(Long accountId, PostSearchCond cond, Pageable pageable) {
        return postRepository.searchPost(cond, pageable)
                             .stream( )
                             .map(post -> new PostViewer(accountId, post))
                             .map(postViewer -> conversionService.convert(postViewer, PostResponse.class))
                             .collect(Collectors.toList( ));
    }
    
    public PostResponse getPost(Long postId, Long accountId) {
        var comments = commentService.findAllByPostId(postId, accountId);
        return postRepository.findByIdWithWriterAndStockAndLikes(postId)
                             .map(post -> conversionService.convert(post, PostResponse.class))
                             .map(response -> response.setLike(accountId))
                             .map(response -> response.setComments(comments))
                             .orElseThrow(( ) -> new NonExistentResourceException("post", postId));
    }
    
    @Transactional
    public PostResponse viewPost(Long postId, Long accountId) {
        var comments = commentService.findAllByPostId(postId, accountId);
        return postRepository.findByIdWithWriterAndStockAndLikes(postId)
                             .map(Post::plusViewCount)
                             .map(post -> conversionService.convert(post, PostResponse.class))
                             .map(response -> response.setLike(accountId))
                             .map(response -> response.setComments(comments))
                             .orElseThrow(( ) -> new NonExistentResourceException("post", postId));
    }
    
    public void deletePost(Long postId, Long accountId) {
        postRepository.findWithWriterById(postId)
                      .filter(post -> post.getWriter( ).getId( ).equals(accountId))
                      .ifPresentOrElse(postRepository::delete, ( ) -> {
                          throw new InvalidRequestException("권한이 없어 게시물을 삭제할 수 없습니다.");
                      });
    }
    
    public LikeResponse like(Long postId, Long accountId) {
        var like = likeRepository.findByPostIdAndAccountId(postId, accountId);
        if (like.isPresent( )) {
            return conversionService.convert(like.get( ), LikeResponse.class).setLike( );
        }
        var post    = conversionService.convert(postId, Post.class);
        var account = conversionService.convert(accountId, Account.class);
        var newLike = likeRepository.save(new PostLike(account, post));
        return conversionService.convert(newLike, LikeResponse.class).setLike( );
    }
    
    public LikeResponse dislike(Long postId, Long accountId) {
        var like = likeRepository.findByPostIdAndAccountId(postId, accountId);
        like.ifPresent(likeRepository::delete);
        return conversionService.convert(like.get( ), LikeResponse.class).setDislike( );
    }
}
