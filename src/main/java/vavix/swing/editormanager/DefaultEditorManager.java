/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.swing.editormanager;


/**
 * �����̃G�f�B�^���Ǘ�����N���X�̃f�t�H���g�̎����N���X�ł��D
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.10 010906 nsano fix specification??? <br>
 *          0.11 020503 nsano repackage <br>
 */
public class DefaultEditorManager extends EditorManager {

    /**
     * �G�f�B�^���I�[�v�����ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected void editorOpenedImpl(Editor editor) {}

    /**
     * �G�f�B�^���N���[�Y���ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected void editorClosedImpl(Editor editor) {}

    /**
     * �G�f�B�^���A�b�v�f�[�g���ꂽ�Ƃ��ɌĂ΂�鏈���̎����ł��D
     */
    protected void editorUpdatedImpl(Editor editor) {}

    /**
     * �G�f�B�^�}�l�[�W�����g���ăG�f�B�^���I�[�v���������̏����̎����ł��D
     */
    public void openEditor(Editor editor) {}

    /**
     * �G�f�B�^�}�l�[�W�����g���ăG�f�B�^���N���[�Y�������̏����̎����ł��D
     */
    public void closeEditor(Editor editor) {}

    /**
     * �G�f�B�^�}�l�[�W�����g���ăG�f�B�^���A�b�v�f�[�g�������̏����̎����ł��D
     */
    public void updateEditor(Editor editor) {}

    /**
     * �G�f�B�^�}�l�[�W�����g���đS�G�f�B�^���N���[�Y�������̏����̎����ł��D
     */
    protected void closedAllEditorsImpl() {}
}

/* */
