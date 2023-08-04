package hu.ponte.hr.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageMeta {
	private String id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;
	private String path; // Én itt hozzáadtam ezt az útvonalat is, lássuk, hogy hol a kép.
}
