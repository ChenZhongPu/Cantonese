package com.sjtu.chenzhongpu.cantonese;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenzhongpu on 9/20/16.
 */

class SoundBean {
    String sound;
    int audioID;
    SoundBean(String s, int a) {
        sound = s;
        audioID = a;
    }
}
public class CardFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private SoundBean[] shenArray = {new SoundBean("b 波", R.raw.b0), new SoundBean("p 婆", R.raw.p0), new SoundBean("m 摸", R.raw.m0), new SoundBean("f 科", R.raw.f0),
            new SoundBean("d 多", R.raw.d0), new SoundBean("t 拖", R.raw.t0), new SoundBean("n 挪", R.raw.n0), new SoundBean("l 啰", R.raw.l0),
            new SoundBean("g 家", R.raw.g0), new SoundBean("k 卡", R.raw.k0), new SoundBean("h 哈", R.raw.h0), new SoundBean("ng 牙", R.raw.ngaa0),
            new SoundBean("z 支", R.raw.z0), new SoundBean("c 雌", R.raw.c0), new SoundBean("s 思", R.raw.s0), new SoundBean("j 衣", R.raw.j0),
            new SoundBean("gw 瓜", R.raw.gw0), new SoundBean("kw 夸", R.raw.kw0), new SoundBean("w 娃", R.raw.s0)
    };

    private SoundBean[] yunArray = {new SoundBean("aa 丫", R.raw.aa0), new SoundBean("aai 挨", R.raw.aai0), new SoundBean("aau 坳", R.raw.aau0),
            new SoundBean("aam *三", R.raw.aam0), new SoundBean("aan *山", R.raw.aan0), new SoundBean("aang *烹", R.raw.aang0),
            new SoundBean("aap 鸭", R.raw.aap0), new SoundBean("aat 压", R.raw.aat0), new SoundBean("aak 握", R.raw.aak0),
            new SoundBean("ai 矮", R.raw.ai0), new SoundBean("au 欧", R.raw.au0), new SoundBean("am 庵", R.raw.am0),
            new SoundBean("an 奀", R.raw.an0), new SoundBean("ang 莺", R.raw.ang0), new SoundBean("ap 噏", R.raw.ap0),
            new SoundBean("at *不", R.raw.at0), new SoundBean("ak 厄", R.raw.ak0), new SoundBean("e *爹", R.raw.e0),
            new SoundBean("ei *菲", R.raw.ei0), new SoundBean("eu", R.raw.eu0), new SoundBean("em", R.raw.em0), new SoundBean("en", R.raw.en0),
            new SoundBean("eng *厅", R.raw.eng0),new SoundBean("ep", R.raw.ep0), new SoundBean("et", R.raw.et0), new SoundBean("ek *叻", R.raw.ek0),
            new SoundBean("eoi *居", R.raw.eoi0), new SoundBean("eon *津", R.raw.eon0), new SoundBean("eot *卒", R.raw.eot0),
            new SoundBean("oe *靴", R.raw.oe0), new SoundBean("oeng *香", R.raw.oeng0), new SoundBean("oet", R.raw.oet0), new SoundBean("oek *脚", R.raw.oek0),
            new SoundBean("o 柯", R.raw.o0), new SoundBean("oi 哀", R.raw.oi0), new SoundBean("ou 澳", R.raw.ou0), new SoundBean("on 安", R.raw.on0),
            new SoundBean("ong *肮", R.raw.ong0), new SoundBean("ot *渴", R.raw.ot0), new SoundBean("ok 恶", R.raw.ok0),
            new SoundBean("i *衣", R.raw.i0), new SoundBean("iu *腰", R.raw.iu0), new SoundBean("im *签", R.raw.im0), new SoundBean("in *烟", R.raw.in0),
            new SoundBean("ing *英", R.raw.ing0), new SoundBean("ip *叶1", R.raw.ip0), new SoundBean("ik *式", R.raw.ik0), new SoundBean("u *姑", R.raw.u0),
            new SoundBean("ui *杯", R.raw.ui0), new SoundBean("un *搬", R.raw.un0), new SoundBean("ung *冬", R.raw.ung0),
            new SoundBean("ut *阔", R.raw.ut0), new SoundBean("uk 屋", R.raw.uk0), new SoundBean("yu *于", R.raw.yu0), new SoundBean("yun *冤", R.raw.yun0),
            new SoundBean("yut *月", R.raw.yut0), new SoundBean("m 唔", R.raw.m0), new SoundBean("ng 吴", R.raw.ng0),
    };


    private int position;

    public static CardFragment newInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card,container,false);
        GridView gridView = (GridView) rootView.findViewById(R.id.sound_gridview);
        if (position == 0) {
            gridView.setAdapter(new SoundAdapter(rootView.getContext(), new ArrayList<>(Arrays.asList(shenArray))));
        } else if (position == 1){
            gridView.setAdapter(new SoundAdapter(rootView.getContext(), new ArrayList<>(Arrays.asList(yunArray))));
        }

        return rootView;
    }

}

class SoundAdapter extends BaseAdapter {

    private Context mContext;
    private List<SoundBean> mSoundBeanList;

    public SoundAdapter(Context c, List<SoundBean> s) {
        mContext = c;
        mSoundBeanList = s;
    }

    public int getCount() {
        return mSoundBeanList.size();
    }

    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Button button;
//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            button = (Button) inflater.inflate(R.layout.sound_button, null);
            button.setLayoutParams(new GridView.LayoutParams(160, 160));
            button.setPadding(8, 8, 8, 8);
            button.setText(mSoundBeanList.get(position).sound);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(mSoundBeanList.get(position).audioID);
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        afd.close();
                    } catch (Exception e) {
                        Snackbar.make(v.getRootView(), R.string.error_play, Snackbar.LENGTH_SHORT)
                                .setDuration(3000).show();
                    }
                }
            });
//        } else {
//            button = (Button) convertView;
//        }
        return button;
    }

}
