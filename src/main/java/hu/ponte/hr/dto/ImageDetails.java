package hu.ponte.hr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDetails {
	private String id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;
	private String path;// Én itt hozzáadtam ezt az útvonalat is, lássuk, hogy hol a kép.
	private byte[] file;
}
