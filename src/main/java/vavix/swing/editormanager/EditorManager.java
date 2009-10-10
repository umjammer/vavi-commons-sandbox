/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;

import java.util.ArrayList;
import java.util.List;

import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;


/**
 * �����̃G�f�B�^���Ǘ������{�N���X�ł��D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano be abstract class <br>
 *          0.11 020503 nsano repackage <br>
 */
public abstract class EditorManager implements EditorListener {

    /** �����̃G�f�B�^�Ǘ��p�x�N�^ */
    protected volatile List<Editor> editors = new ArrayList<Editor>();

    /** �J�����g�̃G�f�B�^ */
    protected Editor current;

    /**
     * �G�f�B�^���I�[�v�����ꂽ�Ƃ��ɌĂ΂�܂��D
     */
    public void editorOpened(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editors.add(editor);
        current = editor;

        editorOpenedImpl(editor);
    }

    /**
     * �G�f�B�^���I�[�v�����ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected abstract void editorOpenedImpl(Editor editor);

    /**
     * �G�f�B�^���N���[�Y���ꂽ�Ƃ��ɌĂ΂�܂��D
     */
    public void editorClosed(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editors.remove(editor);

        int size = editors.size();
        if (size == 0) {
            current = null;
        } else {
            current = editors.get(size - 1);
        }

        editorClosedImpl(editor);
    }

    /**
     * �G�f�B�^���N���[�Y���ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected abstract void editorClosedImpl(Editor editor);

    /**
     * �G�f�B�^���A�b�v�f�[�g���ꂽ�Ƃ��ɌĂ΂�܂��D
     */
    public void editorUpdated(EditorEvent ev) {
        Editor editor = (Editor) ev.getSource();
        editorUpdatedImpl(editor);
    }

    /**
     * �G�f�B�^���A�b�v�f�[�g���ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected abstract void editorUpdatedImpl(Editor editor);

    /**
     * �G�f�B�^���I�[�v�����܂��D
     * 
     * @param editor �G�f�B�^
     */
    public abstract void openEditor(Editor editor);

    /**
     * �G�f�B�^���N���[�Y���܂��D
     * 
     * @param editor �G�f�B�^
     */
    public abstract void closeEditor(Editor editor);

    /**
     * �G�f�B�^���A�b�v�f�[�g���܂��D
     * 
     * @param editor �G�f�B�^
     */
    public abstract void updateEditor(Editor editor);

    /**
     * �S�G�f�B�^���N���[�Y���܂��D
     */
    public void closeAllEditors() {
        @SuppressWarnings("unchecked")
        List<Editor> clones = (List<Editor>) ((ArrayList<Editor>) editors).clone();

        for (int i = 0; i < clones.size(); i++) {
            Editor editor = clones.get(i);
            closeEditor(editor);
        }

        closedAllEditorsImpl();
    }

    /**
     * �G�f�B�^�}�l�[�W�����g���đS�G�f�B�^���N���[�Y�����Ƃ��̏����̎����ł��D
     */
    protected abstract void closedAllEditorsImpl();

    /**
     * �G�f�B�^�̃��X�g���擾���܂��D
     * 
     * @return �G�f�B�^�̃��X�g
     */
    public List<Editor> getEditors() {
        return editors;
    }

    /**
     * �J�����g�̃G�f�B�^���擾���܂��D
     */
    public Editor getCurrentEditor() {
        return current;
    }
}

/* */
