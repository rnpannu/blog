// package com.mspannu.blog.mappers;

// import java.util.List;

// import com.mspannu.blog.domain.dtos.CreateCategoryRequest;
// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;
// import org.mapstruct.Named;
// import org.mapstruct.ReportingPolicy;
// import org.mapstruct.factory.Mappers;

// import com.mspannu.blog.domain.PostStatus;
// import com.mspannu.blog.domain.dtos.CategoryDto;
// import com.mspannu.blog.domain.entities.Category;
// import com.mspannu.blog.domain.entities.Post;

// @Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
// public interface CategoryMapper {
//     CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

//     @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
//     CategoryDto toDto(Category category);

//     Category toEntity(CreateCategoryRequest createCategoryRequest);

//     @Named("calculatePostCount")
//     default long calculatePostCount(List<Post> posts){
//         if (null == posts) {
//             return 0;
//         }
//         return posts.stream()
//                 .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
//                 .count();
//     }
// }

package com.mspannu.blog.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.mspannu.blog.domain.PostStatus;
import com.mspannu.blog.domain.dtos.CategoryDto;
import com.mspannu.blog.domain.dtos.CreateCategoryRequest;
import com.mspannu.blog.domain.entities.Category;
import com.mspannu.blog.domain.entities.Post;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source="posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if(null == posts) {
            return 0;
        }
        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}

