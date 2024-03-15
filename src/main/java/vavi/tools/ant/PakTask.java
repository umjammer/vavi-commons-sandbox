/*
 * Copyright (c) 2004 by Naohide Sano, All Rights Reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.tools.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import vavi.util.iappli.Pak;


/**
 * Pak.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 040125 nsano initial version <br>
 */
public class PakTask extends Task {

    /** */
    private String imageList;

    /** */
    public void setImageList(String imageList) {
        this.imageList = imageList;
    }

    /** */
    private String soundList;

    /** */
    public void setSoundList(String soundList) {
        this.soundList = soundList;
    }

    /** */
    private String messages;

    /** */
    public void setMessages(String messages) {
        this.messages = messages;
    }

    /** */
    private String output;

    /** */
    public void setOutput(String output) {
        this.output = output;
    }

    /** */
    public void execute() {
        try {
            Pak pak = new Pak();
            pak.setOutput(output);
            pak.setMessagesFilename(messages);
            pak.setImageListFilename(imageList);
            pak.setSoundListFilename(soundList);
            pak.create();
        } catch (Exception e) {
e.printStackTrace(System.err);
            throw new BuildException("Pak Error", e);
        }
    }
}
