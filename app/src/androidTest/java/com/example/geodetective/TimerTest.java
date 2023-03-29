package com.example.geodetective;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ActivityScenario;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimerTest {

    @Rule
    public ActivityScenario Overview = ActivityScenario.launch(QuestOverviewActivity.class);
    @Mock
    private Context context;
    @Mock
    private ViewGroup viewGroup;
    @Mock
    private View view;

    @Test
    public void invalidConstructor() {
        try{
            Timer timer = new Timer(null, null);
            fail("Timer constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    @Test
    public void addAddingTest() {
        // Arrange
        when(viewGroup.getContext()).thenReturn(context);

        // Act
        Timer timer = new Timer(context, viewGroup);
//        MyObject.addObjectToViewGroup(viewGroup, view);

        // Assert
        verify(viewGroup).addView(view);
    }

}
