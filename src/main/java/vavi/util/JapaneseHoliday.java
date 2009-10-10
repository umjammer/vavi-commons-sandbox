/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.util.Calendar;


/**
 * ���{�ɂ����鍑���̏j�����擾����N���X�ł��B
 * <pre>
 * 1��1��          ���U
 * 1����2���j��    ���l�̓�
 * 2��11��         �����L�O�̓�
 * 3��*��          �t���̓�
 * 4��29��         �݂ǂ�̓�
 * 5��3��          ���@�L�O��
 * 5��4��          �����̋x��
 * 5��5��          ���ǂ��̓�
 * 7��20��         �C�̓� (2003�N�����O���j��)
 * 9��15��         �h�V�̓� (2003�N�����O���j��)
 * 9��*��          �H���̓�
 * 10����2���j��   �̈�̓�
 * 11��3��         �����̓�
 * 11��23��        �ΘJ���ӂ̓�
 * 12��23��        �V�c�a����
 * </pre>
 *
 * @author	<a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version	0.00	021119	nsano	port <br>
 */
public class JapaneseHoliday {

    /** ���̃I�u�W�F�N�g���w�����t */
    private Calendar calendar;

    /** �y�j�����x���Ƃ��邩�ǂ��� */
    private boolean saturdayHoliday;

    /**
     * �f�t�H���g�̃^�C���]�[������у��P�[�������J�����_�[���g�p���܂��B
     */
    public JapaneseHoliday() {
        this.calendar = Calendar.getInstance();
    }

    /**
     * �y�j�����x���Ƃ��邩�ǂ�����ݒ肵�܂��B
     * @param sturdayHoliday �y�j�����x���Ƃ���Ȃ� true�A�����łȂ��Ȃ� false
     */
    public void setSaturdayHoliday(boolean sturdayHoliday) {
        this.saturdayHoliday = sturdayHoliday;
    }

    /**
     * ���̃C���X�^���X���쐬���������̓��t���x�����ǂ����𔻒肵�܂��B
     * @return �x���Ȃ�� true�A�����łȂ��Ȃ� false
     */
    public boolean isHoliday() {
        return isHoliday(calendar.get(Calendar.YEAR),
                         calendar.get(Calendar.MONTH),
                         calendar.get(Calendar.DAY_OF_MONTH));
        
    }

    /**
     * �w�肳�ꂽ�N�������x�����ǂ������肵�܂��B
     * �I�u�W�F�N�g�̃J�����_�[�͎w�肳�ꂽ���ɕύX����܂��B
     * @param yyyy �N�A���� 4 ���Ŏw��
     * @param mm ���A10 ���̏ꍇ�� 9 ���w�� 
     * @param dd ��
     * @return �x���Ȃ�� true�A�����łȂ��Ȃ� false
     */
    public boolean isHoliday(int yyyy, int mm, int dd) {
        int[] equinox = getEquinoxDays(yyyy);
        
        int[] monday = getMondayHoliday(yyyy);
        mm++;
        // �킩��₷�� cal.set(Calendar.YEAR, yyyy);
        calendar.set(Calendar.MONTH, mm - 1);
        calendar.set(Calendar.DAY_OF_MONTH, dd);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else if (saturdayHoliday && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        
        if ((mm == 1 && (dd == 1 || dd == monday[0])) ||
            (mm == 2 && (dd == 11)) ||
            (mm == 3 && (dd == equinox[0])) ||
            (mm == 4 && (dd == 29)) ||
            (mm == 5 && (dd == 3 || dd == 4 || dd == 5)) ||
            (mm == 7 && (dd == monday[1])) ||
            (mm == 9 && (dd == monday[2] || dd == equinox[1])) ||
            (mm == 10 && (dd == monday[3])) ||
            (mm == 11 && (dd == 3 || dd == 23)) ||
            (mm == 12 && (dd == 23))) {
            return true;
        } else {
            if (mm == 2 && dd == 12) {
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 11);
                
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if
                (mm == 3 && dd == (equinox[0] + 1)) {
                calendar.set(Calendar.MONTH, 2);
                
                calendar.set(Calendar.DAY_OF_MONTH, equinox[0]);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 4 && dd == 30) {
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DAY_OF_MONTH, 29);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 5 && dd == 6) {
                calendar.set(Calendar.MONTH, 4);
                calendar.set(Calendar.DAY_OF_MONTH, 5);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 9 && dd == (equinox[1] + 1)) {
                calendar.set(Calendar.MONTH, 8);
                calendar.set(Calendar.DAY_OF_MONTH, equinox[1]);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 11 && dd == 4) {
                calendar.set(Calendar.MONTH, 10);
                calendar.set(Calendar.DAY_OF_MONTH, 3);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 11 && dd == 24) {
                calendar.set(Calendar.MONTH, 10);
                calendar.set(Calendar.DAY_OF_MONTH, 23);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else if (mm == 12 && dd == 24) {
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DAY_OF_MONTH, 23);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return true;
                }
            } else {
                if (yyyy <= 2002) {
                    if (mm == 7 && dd == (monday[1] + 1)) {
                        calendar.set(Calendar.MONTH, 6);
                        calendar.set(Calendar.DAY_OF_MONTH, monday[1]);
                        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                            return true;
                        }
                    } else if (mm == 9 && (dd == monday[2] + 1)) {
                        calendar.set(Calendar.MONTH, 8);
                        calendar.set(Calendar.DAY_OF_MONTH, monday[2]);
                        if (calendar.get(Calendar.DAY_OF_WEEK) ==
                            Calendar.SUNDAY) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * @param yyyy
     * @return �t���A�H���̓�
     */
    private int[] getEquinoxDays(int yyyy) {
        long temp = 0;
        // �ꎞ�I�ȕϐ� temp = (242194 * (yyyy - 1980));
        
        temp -= ((yyyy - 1980) >> 2) * 1000000;
        long s3 = (20843100 + temp) / 1000000;
        
        long s9 = (23248800 + temp) / 1000000;
        String s3s = String.valueOf(s3);
        // s3s =
        s3s.substring(0, s3s.indexOf('.'));
        String s9s = String.valueOf(s9);
        // s9s =
        s9s.substring(0, s9s.indexOf('.'));
        return new int[] {
            Integer.parseInt(s3s),
            Integer.parseInt(s9s)
        };
    }
    
    /**
     * @param yyyy
     * @return �w�肵���N�̌��j���̋x��
     */
    private int[] getMondayHoliday(int yyyy) {
        int h1 = 0;
        int h7 = 0;
        int h9 = 0;
        int h10 = 0;
        // ���l�̓� 1����񌎗j�� int h7 = 20;
        // �C�̓� 7����O���j��(2003�N����) int h9 = 15;
        // �h�V�̓� 9����O���j��(2003�N����) int h10 = 0;
        // �̈�̓� 10����񌎗j�� cal.set(Calendar.YEAR, yyyy);
        
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
        case 0:
            h1 = 9;
            break;
        case 1:
            h1 = 8;
            break;
        case 2:
            h1 = 14;
            break;
        case 3:
            h1 = 13;
            break;
        case 4:
            h1 = 12;
            break;
        case 5:
            h1 = 11;
            break;
        case 6:
            h1 = 10;
            break;
        }
        
        calendar.set(Calendar.MONTH, 9);
        week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
        case 0:
            h10 = 9;
            break;
        case 1:
            h10 = 8;
            break;
        case 2:
            h10 = 14;
            break;
        case 3:
            h10 = 13;
            break;
        case 4:
            h10 = 12;
            break;
        case 5:
            h10 = 11;
            break;
        case 6:
            h10 = 10;
            break;
        }
        
        if (yyyy >= 2003) {
            calendar.set(Calendar.MONTH, 6);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            switch (week) {
            case 0:
                h7 = 16;
                break;
            case 1:
                h7 = 15;
                break;
            case 2:
                h7 = 21;
                break;
            case 3:
                h7 = 20;
                break;
            case 4:
                h7 = 19;
                break;
            case 5:
                h7 = 18;
                break;
            case 6:
                h7 = 17;
                break;
            }
            
            calendar.set(Calendar.MONTH, 8);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            switch (week) {
            case 0:
                h9 = 16;
                break;
            case 1:
                h9 = 15;
                break;
            case 2:
                h9 = 21;
                break;
            case 3:
                h9 = 20;
                break;
            case 4:
                h9 = 19;
                break;
            case 5:
                h9 = 18;
                break;
            case 6:
                h9 = 17;
                break;
            }
        }
        
        return new int[] { h1, h7, h9, h10 };
    }
}

/* */
