package com.graph.db.file.annotation.subscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.constructorsDeclaredIn;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.google.common.collect.Sets;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.GeneToGeneticVariantOutput;

@RunWith(PowerMockRunner.class)
public class GeneToGeneticVariantSubscriberTest {
	
	private GeneToGeneticVariantSubscriber subscriber;
	private CsvDozerBeanWriter beanWriterMock;
	
	@Before
	public void before() {
		suppress(constructorsDeclaredIn(GeneToGeneticVariantSubscriber.class));
		subscriber = new GeneToGeneticVariantSubscriber(null, null);
		
		beanWriterMock = mock(CsvDozerBeanWriter.class);
		subscriber.setBeanWriter(beanWriterMock);
	}

	@Test
	public void whenCorrectDataIsSuppliedThenItIsWrittenOut() throws IOException {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence.TranscriptConsequenceBuilder()
			.geneId("gene_id")
			.variantId("variant_id")
			.build();
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		ArgumentCaptor<GeneToGeneticVariantOutput> argument = ArgumentCaptor.forClass(GeneToGeneticVariantOutput.class);
		verify(beanWriterMock).write(argument.capture());
		assertEquals("gene_id", argument.getValue().getGene_id());
		assertEquals("variant_id", argument.getValue().getVariant_id());
	}
}
