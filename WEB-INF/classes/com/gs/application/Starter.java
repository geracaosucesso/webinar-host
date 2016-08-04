package com.gs.application;

import virtuozo.infra.Async;
import virtuozo.infra.AsyncException;
import virtuozo.infra.HttpClient;
import virtuozo.infra.HttpClient.Endpoint;
import virtuozo.infra.JsonCallback;
import virtuozo.interfaces.HTML;
import virtuozo.interfaces.HTML.Node;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.json.client.JSONValue;

public class Starter implements EntryPoint {
  private int counter = 0;
  
  private String currentUrl;

  @Override
  public void onModuleLoad() {
    doGet();
    Async.get().schedule(new Runnable() {
      @Override
      public void run() {
        doGet();
      }
    }, 20000);
  }

  private void doGet() {
    HttpClient.create(Endpoint.create("host.json").addQueryParam("c", String.valueOf(counter++))).get().send(new JsonCallback() {

      @Override
      public void onSuccess(JSONValue response) {
        String url = response.isObject().get("url").isString().stringValue();
        if(counter == 1){
          renderVideo(url);
        }
        shouldNotify(url);
      }

      @Override
      public void onFailure(AsyncException exception) {

      }
    });
  }

  private void renderVideo(String url) {
    Node video = HTML.find("video-frame");
    video.attribute("src", url);
    this.currentUrl = url;
  }
  
  private void shouldNotify(String url){
    if(url.equals(this.currentUrl)){
      return;
    }
    
    HTML.find("section-content-button").style().display("inherit");
  }
}