package com.dogaozkaraca.rotaryhome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by doga.ozkaraca on 4/4/2015.
 */

public class OfflineNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<DoFeed_Item> mListAppInfo;
    Typeface font;

    public static boolean offline = false;
    public OfflineNewsAdapter(Context ct, List<DoFeed_Item> feedItemList) {
        mContext = ct;
        mListAppInfo = feedItemList;
        font = Typeface.createFromAsset(ct.getAssets(), "fonts/MainClockFont.ttf");

    }
    BlurReader dFragment;
    static View savedView;
    @Override
    public int getCount() {
        return mListAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mListAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
        ImageView ItemImage;
        ImageButton overflow;
        RelativeLayout titleBg;
        ImageView ItemFeedType;
        TextView ItemTitle;
        ImageView postImageIncluded;
        ScrollView scrollView;
        ImageButton likes_retweets;
        TextView likes_retweetstext;
        ImageButton faves_shares_instaheart;
        TextView faves_shares_instaheart_text;

        ///
        RelativeLayout mainView;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DoFeed_Item article = mListAppInfo.get(position);

        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);



            ViewHolder holder  = new ViewHolder();

            convertView = inflater.inflate(R.layout.do_feed_key_item, null);

            holder.ItemImage = (ImageView)convertView.findViewById(R.id.articlePicture);
            holder.mainView = (RelativeLayout)convertView.findViewById(R.id.itemRL);


            holder.scrollView= (ScrollView) convertView.findViewById(R.id.scrollviewitem);
            holder.ItemFeedType = (ImageView)convertView.findViewById(R.id.feedItemType);
            holder.ItemTitle = (TextView)convertView.findViewById(R.id.articleText);
            holder.titleBg= (RelativeLayout) convertView.findViewById(R.id.titleBg);
            holder.postImageIncluded= (ImageView) convertView.findViewById(R.id.postImageIncluded);
            holder.likes_retweets = (ImageButton) convertView.findViewById(R.id.likes_favs);
            holder.likes_retweetstext = (TextView)convertView.findViewById(R.id.likes_favstext);
            holder.faves_shares_instaheart = (ImageButton) convertView.findViewById(R.id.retweets_reshares);
            holder.faves_shares_instaheart_text  = (TextView)convertView.findViewById(R.id.retweets_resharestext);



            convertView.setTag(holder);




        }

        final ViewHolder holder = (ViewHolder)convertView.getTag();

        holder.scrollView.scrollTo(0, 0);
        ObjectAnimator animScrollToBottom = ObjectAnimator.ofInt(holder.scrollView, "scrollY", holder.scrollView.getBottom()+(int)RotaryView.convertDpToPixel(20));
       animScrollToBottom.setDuration(5000);
        animScrollToBottom.setRepeatCount(ValueAnimator.INFINITE);
        animScrollToBottom.start();
        final View finalConvertView = convertView;

        if(article.getType().equals("facebook"))
        {

            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);

            holder.likes_retweets.setVisibility(View.VISIBLE);
            holder.likes_retweetstext.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart_text.setVisibility(View.VISIBLE);
            holder.postImageIncluded.setVisibility(View.GONE);
            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                 dFragment =  openLink.FacebookLink(mContext,article.getPostURL(),article.getFB_UserId(),article.getImageURL(),article.getPostImageURL(),article.getFavOrLikeOrInstaHeartCount(),article.getPostTitle(),article.getSourceName(),true);
                    zoomImageFromThumb(finalConvertView);

                }

            });
            Picasso.with(mContext).load(R.drawable.fb_likebutton).into(holder.likes_retweets);
            holder.faves_shares_instaheart.setImageResource(R.drawable.ic_action_rt_off);
            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {
                holder.likes_retweetstext.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {

                holder.likes_retweetstext.setText("-");
            }


            if (article.getRetweetReshares() != null)
            {
                holder.faves_shares_instaheart_text.setText(article.getRetweetReshares());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");

            }

            Picasso.with(mContext).load(R.drawable.fb_feeditem).into(holder.ItemFeedType);



            if (article.getPostImageURL() != null)
            {
                holder.postImageIncluded.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(article.getPostImageURL()).into(holder.ItemImage);
                try
                {
                 Picasso.with(mContext).load("https://graph.facebook.com/"+article.getFB_UserId()+"/picture?type=large").into(holder.postImageIncluded);

                }
                catch (Exception e)
                {

                }
            }
            else
            {
                try
                {
                    Picasso.with(mContext).load("https://graph.facebook.com/"+article.getFB_UserId()+"/picture?type=large").into(holder.ItemImage);

                }
                catch (Exception e)
                {
                    Picasso.with(mContext).load(R.drawable.personalization).into(holder.ItemImage);

                }
            }
        }
        else if(article.getType().equals("rss"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);

            holder.likes_retweets.setVisibility(View.GONE);
            holder.likes_retweetstext.setVisibility(View.GONE);
            holder.faves_shares_instaheart.setVisibility(View.GONE);
            holder.faves_shares_instaheart_text.setVisibility(View.GONE);
            holder.postImageIncluded.setVisibility(View.GONE);



            Picasso.with(mContext).load(R.drawable.rss_feeditem).into(holder.ItemFeedType);
            try
            {
                Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
            }
            catch (Exception e)
            {

            }
            holder.ItemImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                    try {
                      dFragment = openLink.RSS_Item(mContext, article.getPostURL(), article.getImageURL(),article.getPostTitle(),article.getFavOrLikeOrInstaHeartCount(),article.getRetweetReshares(),article.getSourceName(),true);
                    } catch (Exception e) {
                       dFragment = openLink.RSS_Item(mContext, article.getPostURL(), null,null,article.getFavOrLikeOrInstaHeartCount(),article.getRetweetReshares(),article.getSourceName(),true);
                    }
                    zoomImageFromThumb(finalConvertView);

                }

            });

            holder.ItemImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

        }
        else if(article.getType().equals("twitter"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);
            holder.likes_retweets.setVisibility(View.VISIBLE);
            holder.likes_retweetstext.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart_text.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart.setImageResource(R.drawable.ic_action_fave_off);
            holder.likes_retweets.setImageResource(R.drawable.ic_action_rt_off);
            holder.postImageIncluded.setVisibility(View.GONE);

            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                    if (article.getPostImageURL() != null)
                    {
                      dFragment =  openLink.TwitterLink(mContext, article.getPostURL(), article.getFB_UserId(),article.getPostImageURL(),article.getSourceName(),article.getPostTitle(),article.getFavOrLikeOrInstaHeartCount(),article.getRetweetReshares(),true,article.getImageURL(),article,article.getIfLikedFavedHearted(),article.getIfRetweeted());

                    }
                    else {
                      dFragment =  openLink.TwitterLink(mContext, article.getPostURL(), article.getFB_UserId(),null,article.getSourceName(),article.getPostTitle(),article.getFavOrLikeOrInstaHeartCount(),article.getRetweetReshares(),true,article.getImageURL(),article,article.getIfLikedFavedHearted(),article.getIfRetweeted());
                    }
                    zoomImageFromThumb(finalConvertView);

                }

            });

            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {

                holder.faves_shares_instaheart_text.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");
            }


            if (article.getRetweetReshares() != null)
            {
                holder.likes_retweetstext.setText(article.getRetweetReshares());
            }
            else
            {
                holder.likes_retweetstext.setText("-");

            }

            Picasso.with(mContext).load(R.drawable.twitter_feeditem).into(holder.ItemFeedType);


            if (article.getPostImageURL() != null)
            {
                holder.postImageIncluded.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(article.getPostImageURL()).into(holder.ItemImage);
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.postImageIncluded);
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
                }
                catch (Exception e)
                {

                }
            }

        }
        else if(article.getType().equals("instagram"))
        {

            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);
            holder.postImageIncluded.setVisibility(View.VISIBLE);

            holder.likes_retweets.setVisibility(View.GONE);
            holder.likes_retweetstext.setVisibility(View.GONE);
            holder.faves_shares_instaheart.setVisibility(View.VISIBLE);
            holder.faves_shares_instaheart_text.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(R.drawable.insta_likebutton).into(holder.faves_shares_instaheart);

            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                   dFragment = openLink.InstagramLink(mContext,article.getPostURL(),article.getFB_UserId(),article.getImageURL(),article.getPostImageURL(),article.getFavOrLikeOrInstaHeartCount(),article.getPostTitle(),article.getSourceName(),true);
                    zoomImageFromThumb(finalConvertView);
                }

            });

            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {
                holder.faves_shares_instaheart_text.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");

            }
            holder.postImageIncluded.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(R.drawable.instagram_feeditem).into(holder.ItemFeedType);
            try
            {
                Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
            }
            catch (Exception e)
            {

            }


            Picasso.with(mContext).load(article.getPostImageURL()).into(holder.postImageIncluded);



        }
        else if(article.getType().equals("empty"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (130 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.INVISIBLE);
            holder.ItemTitle.setVisibility(View.INVISIBLE);
            holder.ItemImage.setVisibility(View.INVISIBLE);

            holder.titleBg.setVisibility(View.INVISIBLE);
        }
        else if(article.getType().equals("tumblr"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);
            holder.likes_retweets.setVisibility(View.GONE);
            holder.likes_retweetstext.setVisibility(View.GONE);
            holder.faves_shares_instaheart.setVisibility(View.GONE);
            holder.faves_shares_instaheart_text.setVisibility(View.GONE);
            holder.likes_retweets.setImageResource(R.drawable.ic_action_fave_on);
            holder.faves_shares_instaheart.setImageResource(R.drawable.ic_action_rt_on);
            holder.postImageIncluded.setVisibility(View.GONE);

            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                   dFragment = openLink.TumblrLink(mContext,article.getPostURL(),article.getFB_UserId(),article.getSourceName(),article.getPostTitle(),article.getImageURL(),true);
                    zoomImageFromThumb(finalConvertView);
                }

            });

            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {
                holder.likes_retweetstext.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {
                holder.likes_retweetstext.setText("-");
            }


            if (article.getRetweetReshares() != null)
            {
                holder.faves_shares_instaheart_text.setText(article.getRetweetReshares());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");

            }
            Picasso.with(mContext).load(R.drawable.tumblr_feeditem).into(holder.ItemFeedType);


            if (article.getPostImageURL() != null)
            {
                holder.postImageIncluded.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(article.getPostImageURL()).into(holder.ItemImage);

                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.postImageIncluded);
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
                }
                catch (Exception e)
                {
                    Picasso.with(mContext).load(R.drawable.personalization).into(holder.postImageIncluded);

                }
            }


        }
        else if(article.getType().equals("linkedin"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);
            holder.likes_retweets.setVisibility(View.GONE);
            holder.likes_retweetstext.setVisibility(View.GONE);
            holder.faves_shares_instaheart.setVisibility(View.GONE);
            holder.faves_shares_instaheart_text.setVisibility(View.GONE);
            holder.likes_retweets.setImageResource(R.drawable.ic_action_fave_on);
            holder.faves_shares_instaheart.setImageResource(R.drawable.ic_action_rt_on);
            holder.postImageIncluded.setVisibility(View.GONE);

            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                  dFragment =  openLink.LinkedInLink(mContext,article.getPostURL(),article.getFB_UserId(),true);
                    zoomImageFromThumb(finalConvertView);
                }

            });
            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {
                holder.likes_retweetstext.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {
                holder.likes_retweetstext.setText("-");
            }


            if (article.getRetweetReshares() != null)
            {
                holder.faves_shares_instaheart_text.setText(article.getRetweetReshares());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");

            }

            Picasso.with(mContext).load(R.drawable.linkedin_feeditem).into(holder.ItemFeedType);


            if (article.getPostImageURL() != null)
            {
                holder.postImageIncluded.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(article.getPostImageURL()).into(holder.ItemImage);
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.postImageIncluded);
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
                }
                catch (Exception e)
                {
                    Picasso.with(mContext).load(R.drawable.personalization).into(holder.postImageIncluded);

                }
            }


        }
        else if(article.getType().equals("foursquare"))
        {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            holder.ItemImage.getLayoutParams().height = (int) (220 * scale + 0.5f);;
            holder.ItemFeedType.setVisibility(View.VISIBLE);
            holder.ItemTitle.setVisibility(View.VISIBLE);
            holder.ItemImage.setVisibility(View.VISIBLE);

            holder.titleBg.setVisibility(View.VISIBLE);
            holder.likes_retweets.setVisibility(View.GONE);
            holder.likes_retweetstext.setVisibility(View.GONE);
            holder.faves_shares_instaheart.setVisibility(View.GONE);
            holder.faves_shares_instaheart_text.setVisibility(View.GONE);
            holder.likes_retweets.setImageResource(R.drawable.ic_action_fave_on);
            holder.faves_shares_instaheart.setImageResource(R.drawable.ic_action_rt_on);
            holder.postImageIncluded.setVisibility(View.GONE);

            holder.ItemImage.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    //Used FB_UserId as PostId for Twitter !!! Don't Forget
                    dFragment = openLink.FoursquareLink(mContext, "url", null, article.getSourceName(), article.getPostTitle(), article.getPostImageURL(), false);
                    zoomImageFromThumb(finalConvertView);
                }

            });
            if (article.getFavOrLikeOrInstaHeartCount() != null)
            {
                holder.likes_retweetstext.setText(article.getFavOrLikeOrInstaHeartCount());
            }
            else
            {
                holder.likes_retweetstext.setText("-");
            }


            if (article.getRetweetReshares() != null)
            {
                holder.faves_shares_instaheart_text.setText(article.getRetweetReshares());
            }
            else
            {
                holder.faves_shares_instaheart_text.setText("-");

            }

            Picasso.with(mContext).load(R.drawable.foursquare_feeditem).into(holder.ItemFeedType);


            if (article.getPostImageURL() != null)
            {
                holder.postImageIncluded.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(article.getPostImageURL()).into(holder.ItemImage);
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.postImageIncluded);
                }
                catch (Exception e)
                {

                }
            }
            else
            {
                try
                {
                    Picasso.with(mContext).load(article.getImageURL()).into(holder.ItemImage);
                }
                catch (Exception e)
                {
                    Picasso.with(mContext).load(R.drawable.personalization).into(holder.postImageIncluded);

                }
            }


        }
        holder.ItemImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                articleEditorDialog dialog = new articleEditorDialog(mContext,article.getPostTitle(),article.getPostURL(),position);

                dialog.show();
                return false;
            }
        });

        holder.ItemTitle.setText(article.getPostTitle());
        return convertView;
    }

    private static Animator mCurrentAnimator;


    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private static int mShortAnimationDuration;
    public static float startScale;
    public static Rect startBounds;
    private BlurReader m_dialog = null;
    private void zoomImageFromThumb(final View thumbView) {


        savedView = thumbView;
        mShortAnimationDuration = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) DoWorkspace.mLauncher.findViewById(R.id.art);
        //expandedImageView.setImageResource(R.drawable);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        DoWorkspace.feed.getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        final float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                startBounds.left, finalBounds.left + RotaryView.convertDpToPixel(5)))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top + ((display.getHeight()-RotaryView.convertDpToPixel(400)) / 2 - RotaryView.convertDpToPixel(40))))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale,1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;


                // ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) expandedImageView.getLayoutParams();
                // int margin = (int)RotaryView.convertDpToPixel(5);
                // lp.setMargins(margin,margin,margin,margin);
                // expandedImageView.setLayoutParams(lp);

                OfflineNewsAdapter.startBounds = startBounds;
                OfflineNewsAdapter.startScale = startScale;


                if ((m_dialog == null) || !m_dialog.isVisible()){
                    dFragment.show(RotaryHome.whoami.getFragmentManager(), "BlurReader");
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;


    }

    public static void dismissTheDialogAnim()
    {
        if(savedView != null) {
            final ImageView expandedImageView = (ImageView) DoWorkspace.mLauncher.findViewById(R.id.art);
            // Upon clicking the zoomed-in image, it should zoom back down
            // to the original bounds and show the thumbnail instead of
            // the expanded image.
            final float startScaleFinal = startScale;

            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Animate the four positioning/sizing properties in parallel,
            // back to their original values.
            AnimatorSet set = new AnimatorSet();
            set.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y, startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    savedView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    savedView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;
        }
    }

}
