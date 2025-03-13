package com.vachel.editor;

import android.graphics.Color;
import android.view.View;

import com.vachel.editor.bean.StickerText;
import com.vachel.editor.emoji.Emoji;
import com.vachel.editor.emoji.EmojiDrawer;
import com.vachel.editor.emoji.IEmojiCallback;
import com.vachel.editor.util.Utils;

public class EmojiPicEditActivity extends PictureEditActivity implements IEmojiCallback {

    @Override
    public void initData() {
        mSupportEmoji = true;
    }

    @Override
    public View getStickerLayout() {
            return new EmojiDrawer(this).bindCallback(this);
    }

    @Override
    public void onEmojiClick(String emoji) {
        StickerText stickerText = new StickerText(emoji, Color.WHITE);
        onText(stickerText, false); // emoji其实也是text文本
        Utils.dismissDialog(mStickerImageDialog);
    }

    @Override
    public void onBackClick() {
        mStickerImageDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Emoji.recycleAllEmoji();
    }

    @Override
    public void onSaveSuccess(String savePath) {
        super.onSaveSuccess(savePath);
    }
}
