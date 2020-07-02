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

    public static final int[] selectedVoiceMusic = {
            41, 10, 11,
            12, 13, 14,
            15, 16, 17,
            18, 19, 20
    };

    public static final int[] selectedRepeatDrawable = {
            R.drawable.sunday_seected, R.drawable.monday_selected, R.drawable.tuesday_selected,
            R.drawable.wednesday_selected, R.drawable.thursday_selected, R.drawable.friday_selected,
            R.drawable.saturday_selected
    };

    public static final int[] unSelectedRepeatDrawable = {
            R.drawable.sunday, R.drawable.monday, R.drawable.tuesday,
            R.drawable.wednesday, R.drawable.thursday, R.drawable.friday,
            R.drawable.saturdday
    };

    public static final int wRGB[][]={{0,0,0,0},{255,0,0,0},{0,149, 76, 67},
            {0,145, 86, 40},{0,154, 122, 19},{0,48, 118, 94},
            {0,38, 84, 138},{0,13, 108, 131},{0,143, 93, 154},
            {0,181, 81, 121},{0,0, 0, 0},{0,0, 0, 0},
    };




    public static final byte[] powerOff = new byte[]{0x00, 0x00}; //开灯及声音指令
    public static final byte[] powerOn = new byte[]{0x00, 0x01}; //开灯及声音指令

    //生产环境
    public static final String API_BASE_URL = "http://172.20.1.5:82/";

    //测试连接
    public static final String TEST_LINK = "m.asmx/TestLink";

}
