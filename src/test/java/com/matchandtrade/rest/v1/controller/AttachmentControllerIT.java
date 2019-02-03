package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.rest.v1.transformer.AttachmentTransformer;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import com.matchandtrade.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AttachmentControllerIT extends BaseControllerIT {
	@Autowired
	private AttachmentHelper attachmentHelper;
	@Autowired
	private ArticleHelper articleHelper;
	private AttachmentTransformer attachmentTransformer = new AttachmentTransformer();

	@Before
	public void before() {
		super.before();
	}

	@Test
	public void get_When_AttachmentExists_Then_ReturnAttachment() throws Exception {
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity();
		MockHttpServletResponse response = mockMvc.perform(
			get("/matchandtrade-api/v1/attachments/{attachmentId}", existingAttachment.getAttachmentId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();
		AttachmentJson actual = JsonUtil.fromString(response.getContentAsString(), AttachmentJson.class);
		AttachmentJson expected = attachmentTransformer.transform(existingAttachment);
		assertEquals(expected, actual);
	}

	@Test
	public void get_When_GivenArticleId_Then_ReturnsAllArticleAttachments() throws Exception {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity(existingArticle);
		String response = mockMvc.perform(
			get("/matchandtrade-api/v1/attachments?articleId={articleId}", existingArticle.getArticleId())
				.header(HttpHeaders.AUTHORIZATION, authorizationHeader)
			)
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString();

		List<AttachmentJson> actualList = JsonUtil.fromArrayString(response, AttachmentJson.class);
		AttachmentJson expectedAttachment = attachmentTransformer.transform(existingAttachment);
		assertEquals(expectedAttachment, actualList.get(0));
	}

	@Test
	public void post_When_NewArticle_Then_Succeeds() throws Exception {
		MockMultipartFile multipartFile = attachmentHelper.newMockMultiPartFileImage(MediaType.IMAGE_PNG_VALUE);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.multipart("/matchandtrade-api/v1/attachments/")
			.file(multipartFile);
		MockHttpServletResponse response = mockMvc.perform(request.header(HttpHeaders.AUTHORIZATION, authorizationHeader))
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse();
		AttachmentJson actual = JsonUtil.fromString(response.getContentAsString(), AttachmentJson.class);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getName());
		assertEquals(MediaType.IMAGE_PNG_VALUE, actual.getContentType());
	}
}