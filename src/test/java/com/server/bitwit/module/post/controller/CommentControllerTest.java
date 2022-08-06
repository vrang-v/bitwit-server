package com.server.bitwit.module.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.CreateEmailAccountRequest;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.post.dto.CreateCommentRequest;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.module.post.dto.PostResponse;
import com.server.bitwit.module.post.dto.UpdateCommentRequest;
import com.server.bitwit.module.post.repository.CommentRepository;
import com.server.bitwit.module.post.service.CommentService;
import com.server.bitwit.module.post.service.PostService;
import com.server.bitwit.module.security.AccountPrincipal;
import com.server.bitwit.restdocs.RestDocsConfig;
import com.server.bitwit.util.MockJwt;
import com.server.bitwit.util.MockMvcTest;
import com.server.bitwit.util.RestDocsTest;
import com.server.bitwit.util.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@MockMvcTest
@Transactional
class CommentControllerTest {
    
    @Autowired EntityManager entityManager;
    
    @Autowired MockMvc      mockMvc;
    @Autowired MockJwt      mockJwt;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired PostService    postService;
    @Autowired CommentService commentService;
    @Autowired AccountService accountService;
    
    @Autowired CommentRepository commentRepository;
    
    @Test
    @WithMockAccount
    @DisplayName("댓글 생성 / 201")
    void createComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var request = new CreateCommentRequest( );
        request.setContent("comment-content");
        request.setPostId(post.getId( ));
        
        
        // when
        mockMvc.perform(post("/api/comments")
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isCreated( ),
                       jsonPath("$.commentId").exists( ),
                       jsonPath("$.content").value("comment-content"),
                       jsonPath("$.postId").value(post.getId( )),
                       jsonPath("$.writer.accountId").value(mockAccountId)
               );
    }
    
    @RestDocsTest
    @WithMockAccount
    @DisplayName("자식 댓글 생성 / 201")
    void createChildComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var parentCommentRequest = new CreateCommentRequest( );
        parentCommentRequest.setPostId(post.getId( ));
        parentCommentRequest.setContent("parent-comment-content");
        parentCommentRequest.setAccountId(mockAccountId);
        var parentComment = commentService.createComment(parentCommentRequest);
        
        var request = new CreateCommentRequest( );
        request.setContent("child-comment-content");
        request.setPostId(post.getId( ));
        request.setParentId(parentComment.getId( ));
        
        // then
        mockMvc.perform(post("/api/comments")
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isCreated( ),
                       jsonPath("$.commentId").exists( ),
                       jsonPath("$.content").value("child-comment-content"),
                       jsonPath("$.postId").value(post.getId( )),
                       jsonPath("$.parentId").value(parentComment.getId( )),
                       jsonPath("$.depth").value(1),
                       jsonPath("$.writer.accountId").value(mockAccountId)
               )
               .andDo(document("create-comment",
                       requestFields(
                               fieldWithPath("content").description("댓글 내용"),
                               fieldWithPath("postId").description("게시글 아이디"),
                               fieldWithPath("parentId").description("부모 댓글 아이디").optional( )
                       ),
                       responseFields(
                               fieldWithPath("commentId").description("댓글 아이디"),
                               fieldWithPath("content").description("댓글 내용"),
                               fieldWithPath("postId").description("원본 게시글 아이디"),
                               fieldWithPath("parentId").description("부모 댓글 아이디"),
                               fieldWithPath("children").description("자식 댓글 목록"),
                               fieldWithPath("depth").description("댓글 깊이"),
                               fieldWithPath("edited").description("댓글 수정 여부"),
                               fieldWithPath("deleted").description("댓글 삭제 여부"),
                               fieldWithPath("like").description("댓글 좋아요 여부"),
                               fieldWithPath("likeCount").description("댓글 좋아요 수"),
                               fieldWithPath("createdAt").description("댓글 생성 시각"),
                               fieldWithPath("writer.accountId").description("댓글 작성자 아이디"),
                               fieldWithPath("writer.name").description("댓글 작성자 이름"),
                               fieldWithPath("writer.profileImage").description("댓글 작성자 프로필 이미지"),
                               fieldWithPath("writer.email").description("댓글 작성자 이메일"),
                               fieldWithPath("writer.accountType").description("댓글 작성자 계정 유형"),
                               fieldWithPath("writer.emailVerified").description("댓글 작성자 이메일 인증 여부")
                       )
               ));
    }
    
    @RestDocsTest
    @WithMockAccount
    @DisplayName("댓글 수정 / 204")
    void updateComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var commentRequest = new CreateCommentRequest( );
        commentRequest.setPostId(post.getId( ));
        commentRequest.setContent("comment-content");
        commentRequest.setAccountId(mockAccountId);
        var comment = commentService.createComment(commentRequest);
        
        var request = new UpdateCommentRequest( );
        request.setContent("new-comment-content");
        
        // when
        mockMvc.perform(patch("/api/comments/{commentId}", comment.getId( ))
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(status( ).isNoContent( ))
               .andDo(document("update-comment",
                       pathParameters(
                               parameterWithName("commentId").description("댓글 아이디")
                       ),
                       requestFields(
                               fieldWithPath("content").description("댓글 내용")
                       )
               ));
        
        //then
        var updatedComment = commentRepository.findById(comment.getId( )).orElseThrow( );
        assertThat(updatedComment.getContent( )).isEqualTo("new-comment-content");
        assertThat(updatedComment.isEdited( )).isTrue( );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("다른 유저의 댓글 수정 / 403")
    void updateComment_otherUserComment_403( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var otherAccount  = createAccount( );
        var post          = createPost(mockAccountId);
        
        var commentRequest = new CreateCommentRequest( );
        commentRequest.setPostId(post.getId( ));
        commentRequest.setContent("comment-content");
        commentRequest.setAccountId(otherAccount.getId( ));
        var comment = commentService.createComment(commentRequest);
        
        var request = new UpdateCommentRequest( );
        request.setContent("new-comment-content");
        
        // then
        mockMvc.perform(patch("/api/comments/{commentId}", comment.getId( ))
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isForbidden( ),
                       jsonPath("$.code").value(ErrorCode.NO_PERMISSION.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("존재하지 않는 댓글 수정 / 404")
    void updateComment_nonexistentComment( ) throws Exception {
        // given
        var request = new UpdateCommentRequest( );
        request.setContent("new-comment-content");
        
        // then
        mockMvc.perform(patch("/api/comments/{commentId}", 1)
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("댓글 삭제 / 204")
    void deleteComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var commentRequest = new CreateCommentRequest( );
        commentRequest.setPostId(post.getId( ));
        commentRequest.setContent("comment-content");
        commentRequest.setAccountId(mockAccountId);
        var comment = commentService.createComment(commentRequest);
        
        // when
        mockMvc.perform(delete("/api/comments/{commentId}", comment.getId( ))
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpect(status( ).isNoContent( ))
               .andDo(document("delete-comment",
                       pathParameters(
                               parameterWithName("commentId").description("댓글 아이디")
                       )
               ));
        
        // then
        assertThat(commentRepository.findById(comment.getId( ))).isEmpty( );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("자식 댓글이 있는 댓글 삭제 / 204")
    void deleteComment_hasChildComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var parentCommentRequest = new CreateCommentRequest( );
        parentCommentRequest.setPostId(post.getId( ));
        parentCommentRequest.setContent("root-comment-content");
        parentCommentRequest.setAccountId(mockAccountId);
        var parentComment = commentService.createComment(parentCommentRequest);
        
        var childCommentRequest = new CreateCommentRequest( );
        childCommentRequest.setPostId(post.getId( ));
        childCommentRequest.setContent("child-comment-content");
        childCommentRequest.setAccountId(mockAccountId);
        childCommentRequest.setParentId(parentComment.getId( ));
        commentService.createComment(childCommentRequest);
        
        entityManager.flush( );
        entityManager.clear( );
        
        // when
        mockMvc.perform(delete("/api/comments/{commentId}", parentComment.getId( ))
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpect(status( ).isNoContent( ));
        
        // then
        var deleted = commentRepository.findById(parentComment.getId( )).orElseThrow( );
        assertThat(deleted.isDeleted( )).isTrue( );
        assertThat(deleted.getChildren( )).hasSize(1);
    }
    
    @Test
    @WithMockAccount
    @DisplayName("삭제된 댓글의 자식 댓글 삭제 시 부모 댓글도 함께 삭제 / 204")
    void deleteComment_deletedCommentsChildComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        
        var parentCommentRequest = new CreateCommentRequest( );
        parentCommentRequest.setPostId(post.getId( ));
        parentCommentRequest.setContent("root-comment-content");
        parentCommentRequest.setAccountId(mockAccountId);
        var parentComment = commentService.createComment(parentCommentRequest);
        
        var childCommentRequest = new CreateCommentRequest( );
        childCommentRequest.setPostId(post.getId( ));
        childCommentRequest.setContent("child-comment-content");
        childCommentRequest.setAccountId(mockAccountId);
        childCommentRequest.setParentId(parentComment.getId( ));
        var childComment = commentService.createComment(childCommentRequest);
        
        commentService.deleteComment(parentComment.getId( ), mockAccountId);
        
        entityManager.flush( );
        entityManager.clear( );
        
        // when
        mockMvc.perform(delete("/api/comments/{commentId}", childComment.getId( ))
                       .contentType(APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpect(status( ).isNoContent( ));
        
        // then
        var comments = commentRepository.findAll( );
        assertThat(comments).isEmpty( );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("다른 유저의 Comment 삭제 / 403")
    void deleteComment_otherUserComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var otherAccount  = createAccount( );
        var post          = createPost(mockAccountId);
        
        var otherUserCommentRequest = new CreateCommentRequest( );
        otherUserCommentRequest.setPostId(post.getId( ));
        otherUserCommentRequest.setContent("comment-content");
        otherUserCommentRequest.setAccountId(otherAccount.getId( ));
        var otherUserComment = commentService.createComment(otherUserCommentRequest);
        
        // then
        mockMvc.perform(delete("/api/comments/{commentId}", otherUserComment.getId( ))
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isForbidden( ),
                       jsonPath("$.code").value(ErrorCode.NO_PERMISSION.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("존재하지 않는 Comment 삭제 / 404")
    void deleteComment_nonexistentComment( ) throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", 1)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("댓글 좋아요 / 200")
    void likeComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        var request       = new CreateCommentRequest( );
        request.setPostId(post.getId( ));
        request.setContent("comment-content");
        request.setAccountId(mockAccountId);
        var comment = commentService.createComment(request);
        
        // then
        mockMvc.perform(post("/api/comments/{commentId}/like", comment.getId( ))
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isOk( ),
                       jsonPath("$.like").value(true),
                       jsonPath("$.likeId").exists( ),
                       jsonPath("$.accountId").value(mockAccountId)
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("존재하지 않는 댓글 좋아요 / 404")
    void likeComment_nonexistentComment( ) throws Exception {
        mockMvc.perform(post("/api/comments/{commentId}/like", 1)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("댓글 좋아요 취소 / 200")
    void dislikeComment( ) throws Exception {
        // given
        var mockAccountId = getMockAccountId( );
        var post          = createPost(mockAccountId);
        var request       = new CreateCommentRequest( );
        request.setPostId(post.getId( ));
        request.setContent("comment-content");
        request.setAccountId(mockAccountId);
        var comment = commentService.createComment(request);
        
        commentService.like(comment.getId( ), mockAccountId);
        
        // then
        mockMvc.perform(delete("/api/comments/{commentId}/like", comment.getId( ))
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isOk( ),
                       jsonPath("$.like").value(false),
                       jsonPath("$.likeId").exists( ),
                       jsonPath("$.accountId").value(mockAccountId)
               );
    }
    
    @Test
    @WithMockAccount
    @DisplayName("존재하지 않는 댓글 좋아요 취소 / 404")
    void dislikeComment_nonexistentComment( ) throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}/like", 1)
                       .header(HttpHeaders.AUTHORIZATION, mockJwt.getBearerToken( ))
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    private PostResponse createPost(Long accountId) {
        var request = new CreatePostRequest( );
        request.setAccountId(accountId);
        request.setTitle("title");
        request.setContent("post-content");
        return postService.createPost(request);
    }
    
    private AccountResponse createAccount( ) {
        var request = new CreateEmailAccountRequest( );
        request.setName("test");
        request.setEmail("test@email.com");
        request.setPassword("password");
        return accountService.createEmailAccount(request);
    }
    
    private Long getMockAccountId( ) {
        return ((AccountPrincipal)SecurityContextHolder.getContext( ).getAuthentication( ).getPrincipal( ))
                .getAccount( )
                .getId( );
    }
}
