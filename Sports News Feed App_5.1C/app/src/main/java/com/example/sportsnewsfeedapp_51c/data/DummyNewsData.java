package com.example.sportsnewsfeedapp_51c.data;

import com.example.sportsnewsfeedapp_51c.R;
import com.example.sportsnewsfeedapp_51c.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class DummyNewsData {

    private DummyNewsData() {
    }

    public static List<NewsItem> getNewsItems() {
        List<NewsItem> news = new ArrayList<>();
        news.add(new NewsItem(1, "City Strikers Win Final in Extra Time", "The City Strikers lifted the football cup after a dramatic extra-time goal. The match stayed level until the final minutes, when a fast counter attack created the winning chance.", "Football", R.drawable.placeholder_football, true));
        news.add(new NewsItem(2, "Rovers Sign Young Midfield Star", "The Rovers have announced a new midfield signing after weeks of transfer talks. Coaches say the player brings pace, passing quality, and strong defensive work.", "Football", R.drawable.placeholder_football, false));
        news.add(new NewsItem(3, "National Team Prepares for Qualifier", "The national football squad has started training for the next qualifier. The manager focused on fitness, set pieces, and team shape during the first session.", "Football", R.drawable.placeholder_football, true));
        news.add(new NewsItem(4, "Lions Beat Eagles in Overtime Thriller", "The Lions won a close basketball game in overtime after both teams traded leads in the final quarter. The result keeps the Lions near the top of the table.", "Basketball", R.drawable.placeholder_basketball, true));
        news.add(new NewsItem(5, "Point Guard Records Season-High Assists", "A brilliant passing display helped the Capitals control their latest basketball match. The point guard finished with a season-high assist total and only two turnovers.", "Basketball", R.drawable.placeholder_basketball, false));
        news.add(new NewsItem(6, "Wildcats Coach Praises Defensive Effort", "The Wildcats coach praised the team's defensive focus after holding their opponent below their average score. Rebounding and pressure on the ball made the difference.", "Basketball", R.drawable.placeholder_basketball, false));
        news.add(new NewsItem(7, "Cricket Opener Hits Brilliant Century", "The home side made a strong start after their opening batter scored a patient century. The innings included clean drives, quick singles, and smart strike rotation.", "Cricket", R.drawable.placeholder_cricket, true));
        news.add(new NewsItem(8, "Fast Bowler Returns from Injury", "A leading fast bowler has returned to training after a month on the sidelines. Team staff expect a careful workload before the next cricket series begins.", "Cricket", R.drawable.placeholder_cricket, false));
        news.add(new NewsItem(9, "Spinners Dominate on Turning Pitch", "Spin bowling decided the match as batters struggled to score freely on a dry surface. The captain said patience and field placement were key to the win.", "Cricket", R.drawable.placeholder_cricket, false));
        news.add(new NewsItem(10, "Derby Match Draws Record Crowd", "A record crowd attended the weekend football derby. Fans created a loud atmosphere as both sides pushed for a late winner in a high-intensity contest.", "Football", R.drawable.placeholder_football, false));
        news.add(new NewsItem(11, "Rookie Scores Game-Winning Three", "A rookie guard hit the game-winning three-pointer with two seconds left. Teammates credited confidence and extra shooting practice for the clutch moment.", "Basketball", R.drawable.placeholder_basketball, true));
        news.add(new NewsItem(12, "All-Rounder Leads Team to Series Win", "A strong all-round performance helped seal the cricket series. The player contributed quick runs in the middle overs and took two important wickets.", "Cricket", R.drawable.placeholder_cricket, true));
        return news;
    }
}
