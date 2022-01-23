package com.xxmicloxx.NoteBlockAPI.model;

import java.io.File;

/**
 * Metadata information of the {@link Song}
 */
public class SongMetadata {
    private String title;
    private String author;
    private String originalAuthor;
    private String description;
    private File path;

    public SongMetadata() {
        this.title = "";
        this.author = "";
        this.originalAuthor = "";
        this.description = "";
        this.path = null;
    }

    public SongMetadata(String title, String author, String originalAuthor, String description, File path) {
        this.title = title;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.description = description;
        this.path = path;
    }

    /**
     * Gets the title / name of this Song
     * @return title of the Song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the specified string as Song's new title
     * @param title Name or title of the song
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the Song
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the name of the author of the Song
     * @param author Author's name
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the author of the original Song
     * @return author
     */
    public String getOriginalAuthor() {
        return originalAuthor;
    }

    /**
     * Sets the name of the author of the original Song
     * @param originalAuthor Author's name
     */
    public void setOriginalAuthor(String originalAuthor) {
        this.originalAuthor = originalAuthor;
    }

    /**
     * Gets the description of this Song
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this Song
     * @param description Text that may be shown as a description of the Song
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the File from which this Song is sourced
     * @return file of this Song
     */
    public File getPath() {
        return path;
    }

    /**
     * Sets the file that this Song was loaded from
     * @param path Path of the file
     */
    public void setPath(File path) {
        this.path = path;
    }
}
