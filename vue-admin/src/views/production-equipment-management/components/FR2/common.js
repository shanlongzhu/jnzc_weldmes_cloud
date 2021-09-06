//通道号
export function getChannelNoSourceArr(v) {
  let data = [
    {
      id: "0",
      valueName: "通道1"
    },
    {
      id: "1",
      valueName: "通道2"
    },
    {
      id: "2",
      valueName: "通道3"
    },
    {
      id: "3",
      valueName: "通道4"
    },
    {
      id: "4",
      valueName: "通道5"
    },
    {
      id: "5",
      valueName: "通道6"
    },
    {
      id: "6",
      valueName: "通道7"
    },
    {
      id: "7",
      valueName: "通道8"
    },
    {
      id: "8",
      valueName: "通道9"
    },
    {
      id: "9",
      valueName: "通道10"
    }
  ];
  if (v === 0||v==='0' || v) {
    let filterData = data.filter(item => item.id == v);
    if (filterData.length > 0) {
      return filterData[0].valueName;
    }
  } else {
    return data;
  }
}

//丝径
export function getWireDiameterArr(v) {
  let data = [
    {
      label: "0.8",
      value: 0
    },
    {
      label: "0.9",
      value: 1
    },
    {
      label: "1.0",
      value: 2
    },
    {
      label: "1.2",
      value: 3
    },
    {
      label: "1.4",
      value: 4
    },
    {
      label: "1.6",
      value: 5
    },
    {
      label: "OP1",
      value: 6
    }
  ];
  if (v === 0||v==='0' || v) {
    let filterData = data.filter(item => item.value == v);
    if (filterData.length > 0) {
      return filterData[0].label;
    }
  } else {
    return data;
  }
}

//气体
export function getGasesArr(v) {
  let data = [
    {
      label: "CO2",
      value: 0
    },
    {
      label: "MAG",
      value: 1
    },
    {
      label: "MIG",
      value: 2
    },
    {
      label: "OP1",
      value: 3
    },
    {
      label: "OP2",
      value: 4
    },
    {
      label: "OP3",
      value: 5
    },
    {
      label: "OP4",
      value: 6
    }
  ];
  if (v === 0||v==='0' || v) {
    let filterData = data.filter(item => item.value == v);
    if (filterData.length > 0) {
      return filterData[0].label;
    }
  } else {
    return data;
  }
}
