package com.ra.projectmd05.service.post.hiddenPost;

import com.ra.projectmd05.model.entity.HiddenPost;
import com.ra.projectmd05.model.entity.Post;
import com.ra.projectmd05.model.entity.User;
import com.ra.projectmd05.repository.HiddenPostRepository;
import com.ra.projectmd05.repository.PostRepository;
import com.ra.projectmd05.service.post.PostService;
import com.ra.projectmd05.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HiddenPostServiceImpl implements HiddenPostService{
    private final HiddenPostRepository hiddenPostRepository;
    private final PostService postServiceImpl;
    private final UserService userServiceImpl;
    private final PostRepository postRepository;
    @Override
    public HiddenPost hidePostForUser( Long postId) {

        User user = userServiceImpl.getCurrentUserInfo();
        Post post = postServiceImpl.getPostById(postId);


        if (user != null && post != null) {
            HiddenPost hiddenPost = new HiddenPost();
            hiddenPost.setUser(user);
            hiddenPost.setPost(post);
         return hiddenPostRepository.save(hiddenPost);
        }
        return  null;

    }
}
