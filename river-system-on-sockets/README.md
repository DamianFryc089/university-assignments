# River System On Sockets

## Project Description

The goal of this project is to develop a distributed system that simulates a river network with retention basins, river sections, and environmental controls. The system uses TCP/IP sockets for communication between the different components, with each component running as a separate application on either the same or different computers.

The simulation will model the flow of water through a river system consisting of river sections, retention basins, and an environmental system that generates rainfall. The system allows the user to control the discharge of water from retention basins and simulate the effect of incoming water flow, outflow, and rainfall.

### System Components

#### 1. **Retention Basin (RetensionBasin)**
- **Functionality**: Simulates a retention basin and allows interaction with river sections.
- **Key Features**:
  - Has a maximum volume (`V_i`), a port number, and a connection to a central control center.
  - Communicates with river sections to receive inflow data and sends outflow data to downstream river sections.
  - Allows control of water discharge through sluice gates.
  - Provides methods to get the water discharge, filling percentage, set water discharge, and set water inflow from river sections.
- **Communication**:
  - Exposes an interface `IRetensionBasin` with methods:
    - `int getWaterDischarge()` - Returns the water discharge.
    - `long getFillingPercentage()` - Returns the filling percentage of the basin.
    - `void setWaterDischarge(int waterDischarge)` - Sets the water discharge.
    - `void setWaterInflow(int waterInflow, int port)` - Sets the inflow from a river section based on the port.
    - `void assignRiverSection(int port, string host)` - Assigns a river section to the basin.

#### 2. **River Section (RiverSection)**
- **Functionality**: Simulates a section of a river and communicates with upstream and downstream systems.
- **Key Features**:
  - Has an assigned delay for water flow and communicates with both upstream retention basins and downstream river sections.
  - Receives water discharge data from upstream retention basins and provides inflow data to downstream basins.
  - Allows setting of rainfall for the river section.
- **Communication**:
  - Exposes an interface `IRiverSection` with methods:
    - `void setRealDischarge(int realDischarge)` - Sets the real discharge coming from the upstream retention basin.
    - `void setRainfall(int rainfall)` - Sets the rainfall for the river section.
    - `void assignRetensionBasin(int port, string host)` - Assigns a retention basin to the river section.

#### 3. **Environment (Environment)**
- **Functionality**: Simulates the environmental system that generates rainfall data for river sections.
- **Key Features**:
  - Generates rainfall data and communicates this information to river sections.
- **Communication**:
  - Exposes an interface `IRiverSection` with methods:
    - `void assignRiverSection(int port, string host)` - Assigns a river section to the environmental system.

#### 4. **Control Center (ControlCenter)**
- **Functionality**: Monitors the system, allowing users to monitor the filling levels of retention basins and control the discharge of water.
- **Key Features**:
  - Allows users to monitor the water levels and discharge rates of retention basins.
  - Enables the user to control the sluices (water discharge) of retention basins.
- **Communication**:
  - Exposes an interface `IControlerCenter` with methods:
    - `void assignRetensionBasin(int port, string host)` - Assigns a retention basin to the control center.

### Communication

The system components communicate via TCP/IP sockets. Each component listens for requests on a specified port and interacts with other components using predefined request formats. The communication is based on a simple protocol where requests are encoded as text, and the responses are integer values.

**Request Types**:
- **Get Method**: 
  - `gwd`: Get current water discharge.
  - `gfp`: Get current filling percentage.
  
- **Set Method**: 
  - `swd:<positive integer>`: Set water discharge.
  - `swi:<positive integer>,<positive integer>`: Set water inflow from a river section.
  - `srd:<positive integer>`: Set real discharge from the upstream basin.
  - `srf:<positive integer>`: Set rainfall for the river section.

- **Assign Method**:
  - `arb:<positive integer>,<host>`: Assign a river section to a retention basin.
  - `ars:<positive integer>,<host>`: Assign a retention basin to a river section.

**Request Format**:
```text
request = get method | set method | assign method;
response = positive integer;
