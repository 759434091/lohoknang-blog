package blog.lohoknang.entity;

import java.time.LocalDateTime;

import blog.lohoknang.util.ObjectIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import blog.lohoknang.util.ObjectIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Blog PO
 *
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @Null(groups = InsertBlogGroup.class)
    @NotNull(groups = UpdateBlogGroup.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId id;

    @NotNull(groups = InsertBlogGroup.class)
    @Length(min = 4, max = 10, groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private String category;

    @NotNull(groups = InsertBlogGroup.class)
    @Length(min = 4, max = 10, groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private String author;

    @NotNull(groups = InsertBlogGroup.class)
    @Length(min = 5, max = 60, groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private String title;

    @NotNull(groups = InsertBlogGroup.class)
    @Length(min = 140, groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private String content;

    @Null(groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private String intro;

    @Null(groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    private Integer viewNum;

    @Null(groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    @CreatedDate
    private LocalDateTime createdAt;

    @Null(groups = {InsertBlogGroup.class, UpdateBlogGroup.class})
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
