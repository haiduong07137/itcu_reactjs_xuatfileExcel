import React, { Component } from "react";
import { Breadcrumb } from "egret";
import { Card } from "@material-ui/core";
import BasicMap from "./BasicMap";
import MarkerMap from "./MarkerMap";

class AppMap extends Component {
  state = {};
  render() {
    return (
      <div className="m-sm-30">
        <div  className="mb-sm-30">
          <Breadcrumb routeSegments={[{ name: "Map" }]} />
        </div>
        <Card>
          <BasicMap />
        </Card>
        <div className="py-12" />
        <Card>
          <MarkerMap />
        </Card>
      </div>
    );
  }
}

export default AppMap;
