import React from "react";
import ReactEcharts from "echarts-for-react";
import { merge } from "lodash";

const defaultOption = {
  visualMap: {
    top: 0,
    right: 10,
    pieces: [{
      gt: 0,
      lte: 50,
      color: '#096'
    }, {
      gt: 50,
      lte: 100,
      color: '#ffde33'
    }, {
      gt: 100,
      lte: 150,
      color: '#ff9933'
    }, {
      gt: 150,
      lte: 200,
      color: '#cc0033'
    }],
    outOfRange: {
      color: '#999'
    }
  },
  grid: {
    top: 16,
    left: 24,
    right: 0,
    bottom: 24
  },
  legend: {},
  tooltip: {},
  xAxis: {
    show: true,
    type: "category",
    showGrid: false,
    boundaryGap: false,
  },
  yAxis: {
    type: "value",
    min: 1,
    max: 100,
    splitLine: {
      show: false
    },
    axisLine: {
      show: false
    },
    axisTick: {
      show: false
    },
    axisLabel: {
      color: "rgba(0,0,0,0.54)",
      fontSize: 11,
      fontFamily: "roboto",
    }
  }
};

const ModifiedAreaChart = ({ height, option }) => {
  return (
    <ReactEcharts
      style={{ height: height }}
      option={merge({}, defaultOption, option)}
    />
  );
};

export default ModifiedAreaChart;
