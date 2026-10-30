package com.example.dailymate.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailymate.ui.theme.LavenderPrimary

@Composable
fun ProgressBarCard(
    completedTasksCount: Int,
    totalTasksCount: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalTasksCount > 0) {
        (completedTasksCount.toFloat() / totalTasksCount.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600),
        label = "progressAnimation"
    )

    val percentage = (progress * 100).toInt()

    val quote = when {
        totalTasksCount == 0 -> "No tasks scheduled for today. Time to relax or plan ahead!"
        percentage == 100 -> "Outstanding! All tasks completed for today! 🎉"
        percentage >= 70 -> "You're crushing it! Almost done with today's tasks 💪"
        percentage >= 40 -> "Great progress! Keep up the momentum ⚡"
        else -> "Let's get started and make today productive! ✨"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = LavenderPrimary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Progress",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completedTasksCount of $totalTasksCount tasks completed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = quote,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.95f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
