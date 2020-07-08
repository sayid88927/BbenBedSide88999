/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hosmart.ebaby.base;

import android.os.Environment;

import com.hosmart.ebaby.R;

public class Constant {

    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;

    public static final int[] halfCircle = {
            R.drawable.half_circle_01, R.drawable.half_circle_02, R.drawable.half_circle_03,
            R.drawable.half_circle_04, R.drawable.half_circle_05, R.drawable.half_circle_06,
            R.drawable.half_circle_07, R.drawable.half_circle_08, R.drawable.half_circle_09,
            R.drawable.half_circle_10, R.drawable.half_circle_11
    };

    public static final int[] selectedColorDrawable = {
            R.drawable.color_1_dis, R.drawable.color_2_dis, R.drawable.color_3_dis,
            R.drawable.color_4_dis, R.drawable.color_5_dis, R.drawable.color_6_dis,
            R.drawable.color_7_dis, R.drawable.color_8_dis, R.drawable.color_9_dis,
            R.drawable.color_10_dis, R.drawable.color_11_dis, R.drawable.color_12_dis
    };

    public static final int[] unSelectedColorDrawable = {
            R.drawable.color_1, R.drawable.color_2, R.drawable.color_3,
            R.drawable.color_4, R.drawable.color_5, R.drawable.color_6,
            R.drawable.color_7, R.drawable.color_8, R.drawable.color_9,
            R.drawable.color_10, R.drawable.color_11, R.drawable.color_12
    };

    public static final int[] selectedVoiceDrawable = {
            R.drawable.voice_1_dis, R.drawable.voice_2_dis, R.drawable.voice_3_dis,
            R.drawable.voice_4_dis, R.drawable.voice_5_dis, R.drawable.voice_6_dis,
            R.drawable.voice_7_dis, R.drawable.voice_8_dis, R.drawable.voice_9_dis,
            R.drawable.voice_10_dis, R.drawable.voice_11_dis, R.drawable.voice_12_dis
    };

    public static final int[] unSelectedVoiceDrawable = {
            R.drawable.voice_1, R.drawable.voice_2, R.drawable.voice_3,
            R.drawable.voice_4, R.drawable.voice_5, R.drawable.voice_6,
            R.drawable.voice_7, R.drawable.voice_8, R.drawable.voice_9,
            R.drawable.voice_10, R.drawable.voice_11, R.drawable.voice_12
    };

    public static final int[] SelectedMusicDrawable = {
            R.drawable.icon_voice_1, R.drawable.icon_voice_2, R.drawable.icon_voice_3,
            R.drawable.icon_voice_4, R.drawable.icon_voice_5, R.drawable.icon_voice_6,
            R.drawable.icon_voice_7, R.drawable.icon_voice_8, R.drawable.icon_voice_9,
            R.drawable.icon_voice_10, R.drawable.icon_voice_11, R.drawable.icon_voice_12
    };

    public static final int[] selectedVoiceMusic = {
            41, 10, 11,
            12, 13, 14,
            15, 16, 17,
            18, 19, 20
    };

    public static final int[] selectedAlarmMusic = {
            31, 2, 3,
            4, 5, 6,
            7, 8, 9,
            10, 11, 12
    };

    public static final int[] selectedRepeatDrawable = {
            R.drawable.sunday_seected, R.drawable.monday_selected, R.drawable.tuesday_selected,
            R.drawable.wednesday_selected, R.drawable.thursday_selected, R.drawable.friday_selected,
            R.drawable.saturday_selected
    };

    public static final String[] selectedWeekString = {
            "Su", "M", "Tu", "W", "Th", "F", "Sa"
    };

    public static final int[] unSelectedRepeatDrawable = {
            R.drawable.sunday, R.drawable.monday, R.drawable.tuesday,
            R.drawable.wednesday, R.drawable.thursday, R.drawable.friday,
            R.drawable.saturdday
    };

    public static final int wRGB[][] =
            {{0, 0, 0, 0}, {255, 0, 0, 0}, {0, 255, 0, 0},
            {0, 255, 215, 0}, {0, 255, 255, 0}, {0, 0, 255, 0},
            {0, 0, 0, 255}, {0, 30, 144, 255}, {0, 160, 30, 240},
            {0, 221, 160, 221}, {255, 0, 0, 0}, {0, 0, 0, 0},
    };

    public static final byte[] powerOff = new byte[]{0x00, 0x00}; //开灯及声音指令
    public static final byte[] powerOn = new byte[]{0x00, 0x01}; //开灯及声音指令

}
