package com.gmail.supersonicleader;

import com.gmail.supersonicleader.controller.HomeWorkController;
import com.gmail.supersonicleader.controller.impl.HomeWorkControllerImpl;

public class App {

    public static void main(String[] args) {
        HomeWorkController homeWorkController = HomeWorkControllerImpl.getInstance();
        homeWorkController.runFirstTask();
    }

}
