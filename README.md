# RSM-SDM-project
Spectrally-Spatially Flexible Optical Networks (SS-FONs) testing simulator.

## Quick start - run simulator  

1. Download required files:
    * Download executable `jar` file: `release/RSM-SDM-project-1.0.jar`
    * Download `zip` file with input data for experiments: `data.zip`.
    File `data-password.txt` contains instruction how to extract the archive. Extract archive
    in the same directory as downloaded `jar` file.

2. Download an appropriate JDK (Java Development Kit) for your operating system. The required JDK
version is 11 or newer.
I recommend downloading [Oracle's JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html). 

    Check if the correct java version is installed:

    ```
    $ java -version
    java version "11.0.2" 2019-01-15 LTS
    Java(TM) SE Runtime Environment 18.9 (build 11.0.2+9-LTS)
    Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.2+9-LTS, mixed mode)
    ```

    You need to set the **JAVA_HOME** environment variable to a JDK 11 installation
    directory. To do this, please refer to [this](https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux)
    guide. Alternatively, you can run java providing direct path to its installation
    directory.

3. Run the application

    Application can be run within terminal with:
    
    ```
    $ java -jar RSM-SDM-project-1.0.jar 
    ```
    
    Provide path to example experiments scenario in folder
    `data/experiments/scenario-test.txt` and hit `run` button.
    The results are stored in `results` directory in CSV file format. 
    Alternatively you can view last results using `View Results` button.
    
    The detailed description of input files for experiments is provided further.
    
## Problem background

Spectrally-spatially flexible optical networks (SS-FONs) combine concepts 
of space division multiplexing (SDM) and elastic optical
networks (EONs), and are the answer to the capacity limitations
of currently deployed wavelength division multiplexing (WDM) networks. SS-FONs
provide significant increase in the transmission capacity, adaptive use of various 
modulation formats and flexibility in the management
of spectral and spatial resources. One of the main concerns in
planning and operation of SS-FONs is provisioning of lightpath connections
for traffic demands, which translates into the routing spectrum
and space allocation (RSSA) problem. For more detail about
SS-FON network refer to [1, 2]

## Problem formulation

The optimization problem concerns realization of given demands set
in a spectrally-spatially flexible optical network, where the optimization objective
is defined as required spectrum width. The considered 
SS-FON is defined as a set of network nodes and set of unidirectional network
links that interconnects them. Each network link comprises a set of spatial
modes, e.g., fiber cores, fibers aggregated in the bundle, etc. On each spatial mode,
the available spectrum is divided into small segments (frequency slices) of 
12.5 GHz width. By grouping a subset of adjacent slices, optical
channels (also called super-channels, abbrv. SChs) of different widths can be created.
Therefore, a channel is a set of adjacent slices allocated on the same spatial mode.
Each channel is characterized by a first slice index, number of involved slices and indices
of the applied spatial modes on each edge. 

There is given a set of demands which have to be realized
in the network. Each request is of unicast (one-to-one) transmission technique, i.e.,
a peer-to-peer connection between source and destination node in the
network.

The routing, spectrum and space allocation (RSSA) is a problem of selecting
a routing path and available spectrum and space resources for a given set of demands.
A routing path is a sequence of links that connects
source nodes with the destination node. In order to provision given demand,
despite selection of routing path, also the selection of spatial modes and frequency slices is required, i.e.,
a SCh. Let lightpath denote a combination of given routing path with SCh associated 
with that path, and assigned spatial modes on each link involved in that path. 
Thus, the RSSA problem is a problem of selecting a lightpaths for the given set 
of traffic demands. It is worth noting that selected lightpath has to connect 
source and destination nodes associated with considered demand. Moreover,
 size of the channel depends on the demands requested bit-rate, routing path length,
 applied modulation format and number of utilized spatial modes. When solving RSSA problem,
 three additional constraints have to be satisfied:
 
 * spectrum contiguity constraint - a channel has to be created usign
 adjacent slices on given spatial mode
 * spectrum continuity constraint - a channel has to use the same frequency region
 (same slice indices) on each spatial mode assigned to the lightpath (considering
 all links involved in the routing path)
 * spectrum non-overlapping constraint - given frequency slice may be utilized 
 only by one demand, i.e., more than one channel cannot 
 use the same slice on the same spatial mode of given link.
 
 For a given set of demands and network topology, there may exist 
 multiple solutions for the RSSA problem. Here, the optimization objective is
 the required spectrum width, or equivalently,
 the highest allocated slice index in the network
 among all spatial modes of all links      
 
 For more details about RSSA problem in SS-FONs refer to [3]

## Input experiment files structure

1. Scenario file

    To scenario file defines how to run the experiments. At each line it contains two components
    * path to experiment properties file - a file which defines the simulator setups
    * path to algorithm properties file - (optional) a file which defines parameters for developed algorithm
    
    The scenario file has following form:
    
    ```
    # every line starting with # is a comment line and is ignored
    -ep data/experiments/_test/experiment-test.properties -ap data/experiments/_test/algorithm-test.properties
    ```
    
    The `-ep` switch determines path to experiment properties file.
    The `-ap` swtich determines path to algorithm properties file.
    
    Note, the scenario file may contain more than one line. In such case, lines are processed by the
    simulator consecutively.

2. Experiment properties
    Experiment parameters are defined with Java `.properties` file which has the following structure:
    
    ```
    networkFile=data/networks/US26/ss.net
    candidatePathsFile=data/networks/US26/s.pat
    demandsDirectory=data/networks/US26/unicast-demands/10_000_000---50-1000-50/
    demandsFile=00-02.dem
    slotsForCandidatePathsFile=data/networks/US26/s1.spec
    algorithm=FirstFit
    cores=5
    candidatePaths=20
    maxCandidatePaths=30
    summaryBitrate=100_000:300_000:100_000
    ``` 

    The Java properties file has `key=value` structure.
    Most of the parameters are described further with details.
    Some of the keys allows for values defined as ranges. In such case, the experiments are repeated
    several times for various parameters settings. 
    
    * Integer ranges.
    
    Keys `cores`, `candidatePaths`, and `summaryBitarte` support integer ranges.
    
    It either support multiple integers separated with space or comma, or range of inters defined as
    
    `<start>:<stop>[:<step>]`
    
    where range of integers is generated from `start` to `stop` assuming given `step`. In case when `step` is omitted,
    it is assumed to be equal to 1 or -1 depending whether `start` is greater or smaller than `stop` value.
    
    Below is presented table defining various key-values definitions and tested values of parameter.
    
    |input | output|
    |:---|:---|
    |cores=3 | 3|
    |cores=3,10| 3, 10|
    |cores=2:5|2,3,4,5|
    |cores=3:7:2|3,5,7|
    
    * String ranges
    
    Key `demandsFile` supports multiple strings and strings ranges.
    Firstly, the comma or space separated list of strings will results with running mulitple experimenst.
    For example, `demandsFile=01.dem, 02.dem` will result in running two experiments for `01.dem` value and for `02.dem` 
    value of key `demandsFile`. Secondly, string ranges are supported in the following form:
    
    `<file-name-prefix>[<rangeStart>-<rangeStop>[-<rangeStep>]]<file-name-suffix>`
    
    The inner `[<rangeStart>-<rangeStop>[-<rangeStep>]]` is expanded as in case of integer ranges.
    
    Below is presented table defining various key-values definitions and tested values of parameter.
    
    |input | output|
    |:---|:---|
    |name=file.dat | file.dat|
    |name=file1.dat,file2.dat| file1.dat, file2.dat|
    |name=file00-02.dat|file00.dat, file01.dat, file02.dat|
    |name=file00-04-02.dat|file00.dat, file02.dat, file04.dat|
    
    * Values and limits
    
    The values for fields cannot exceed certain limits which are summarized below:
    
    | key | values| comment|
    |:---|:-----|:---|
    | networkFile|`xx.net` file | where `xx` depends on selected network topology |
    |candidatePathsFile| `x.pat` file |  should correspond to the networksFile folder|
    |demandsDirectory |  |should correspond to the networksFile folder|
    | demandsFile |  |should correspond to the networksFile folder|
    | slotsForCandidatePathsFile | `x1.spec` file  |should correspond to the networksFile folder|
    | algorithm |  FirstFit | |
    | cores | \>= 1 && \<= 50 | |
    | candidatePaths | \>= 1 && \<= 30 | |
    |maxCandidatePaths | =30 | |
    |summaryBitrate | \>= 10_000 && \<= 2_000_000 | |
    
    During experiments all files should be selected for single topology. Let us consider Pol12 network topology.
    Files for that network are placed in folder `data/networks/Pol12/`.
    
    The following properties should be defined as follows:   
    
    ```
    networkFile=pp.net
    candidatePathsFile=p.pat
    slotsForCandidatePathsFile=p1.pat
    ```
    
    The demands directory should be selected within `Pol12` folder. The name of the directory describes the 
    traffic distribution and has following form:
    
    ```unicast-demands/10_000_000---LLL-UUU-SSS/```
    
    where each demand is uniformly distributed with volume within range `LLL` to `UUU` with `SSS` granularity.
    Thus for Pol12, the following folder may be selected:
    
    ```
    demandsDirectory=Pol12/unicast-demands/10_000_000---200-100-200/
    demandsFile=00.dem, 01.dem, 02.dem
    ```
    
3. Algorithm properties

    Algorithm properties files are optional and may be required during extending simulator
    with user defined algorithms. The algorithm properties file is parsed as Java properties file 
    (i.e., with `key=value` pairs).    
    
## Development

1. Requirements 

    To develop the application you need following tools:
    
    * JDK 11 or newer
    
    Verify if you have installed JDK in correct version according to steps in  
    
    * Apache Maven 3.3 or newer
    
    Check if the correct Maven version is installed.
    
    ```
    $ mvn -v
    Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T17:41:47+01:00)
    Maven home: ~/development/maven/maven
    Java version: 11.0.2, vendor: Oracle Corporation
    Java home: ~/development/java/jdk-11.0.2
    Default locale: en_US, platform encoding: ANSI_X3.4-1968
    OS name: "linux", version: "4.13.11-200.fc26.x86_64", arch: "amd64", family: "unix"
    ```
    
    For instruction how to install Apache Maven refer to this [link](https://maven.apache.org/install.html).
    
    Ensure that maven's `bin` directory is added to the `PATH` environment variable. 

2. Building the application

    In the root folder of the application use maven to build it.
    
    ```
    $ mvn clean install 
    [INFO] Scanning for projects...
    [INFO]                                                                         
    [INFO] ------------------------------------------------------------------------
    [INFO] Building RSM-SDM-project 1.0
    [INFO] ------------------------------------------------------------------------
    ...
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ```  

    Maven is responsible for downloading all the required external libraries when they
    are not available in local user repository, thus it may fail when there is no connection
    to the Internet.

## Input network data description

All network data files are stored within `data/networks` folder. Each parsed values are indexed from 0. 

1. xx.net - file with network topology

    File format:
    ```
    Number of nodes
    Number of links
    Matrix node times node with lengths of network links (in kilometers), 0 – link does not exist.
    ```  
    
    Nodes and links are indexed from 0.

2. unicast-demands/TTT---LLL-UUU-SSS/xx.dem - files with unicast demands

    * xx – is the number of file, e.g., 00, 01, etc. There are generated 30 files, should be used to average
    the results.
    * TTT – summary traffic in Gbps
    * LLL – lower bound of generated traffic
    * UUU – upper bound of generated traffic
    * SSS – granularity of generated traffic
    
    For example, folder 200_000---50-1000-50 contains generated demands with the bit-rate randomly
    selected from range 50 Gbps up to 1 Tbps with 50 Gbps granularity.
    
    The unicast request is defined with the source node, destination node and bit-rate given in Gbps.
    
    File format:
    ```
    Number of demands
    Source_node Destination_node bit-rate
    ...
    ```

3. x.pat - file with candidate paths.

    For each pair of network nodes there are generated 30 different candidate paths (*k*). The
    experiments may be performed for different number of candidate paths, e.g., *k*=2, *k*=3, *k*=5, etc. In
    such case, for *k*=2, first 2 paths are selected from 30 available candidate paths, for given pair of
    network nodes.
    
    For a networks with *n* nodes and *e* links (numbered according to the order from network file), each
    path is coded as a sequence of e binary values, where 1 denotes that the link belongs to the path,
    and 0 denotes that the link does not belong to the path.

    File format:
    ```
    Number of all paths
    Path number 1 for pair of nodes (0, 1)
    Path number 2 for pair of nodes (0,1 )
    ...
    Path number 30 for pair of nodes (0, 1)
    Path number 1 for pair of nodes (0, 2)
    Path number 2 for pair of nodes (0, 2)
    ...
    Path number 1 for pair of nodes (2, 0)
    ...
    Path number 30 for pair of nodes (n, n-1)    
    ```
    
4.  x1.spec – file with spectrum requirements for demands

    The file is analogous to the x.pat file and both files should be read jointly. For each candidate path
    in x.pat file, the file x1.spec contains spectrum requirements for that path in the same line number.
    For example, spectrum requirements for *i*-th path in x1.pat (*i+1* line) are available in (*i* line) in
    x1.spec file.
    Each row in x1.spec file consists of 20 columns. First column describes number of slices required for
    demands up to 50 Gbps. Second column describes number of slices for demands with bit-rate
    between 51 Gbps and 100 Gbps. ... The last column (20-th) describes number of slices required for
    the demands with bit-rate between 951 Gbps and 1 Tbps.
    
    File format:
    ```
    [number of slices for 50 Gbps] [number of slices for 100 Gbps] ... [number of slices for 1 Tbps]   
    ```
    
## References

[1] G. M. Saridis, D. Alexandropoulos, G. Zervas, and D. Simeonidou, “Survey and evaluation of space division multiplexing: From
    technologies to optical networks,” <i>IEEE Communications Surveys & Tutorials</i>, vol. 17, no. 4, pp. 2136–2156, Fourthquarter
    2015.
    
[2] M. Klinkowski, P. Lechowicz, and K. Walkowiak, “Survey of resource allocation schemes and algorithms in spectrally-spatially
flexible optical networking,” <i>Optical Switching and Networking</i>, vol. 27, pp. 58–78, 2018.

[3] P. Lechowicz, K. Walkowiak, and M. Klinkowski, “Greedy randomized adaptive search procedure for joint optimization of
    unicast and anycast traffic in spectrally-spatially flexible optical networks,” <i>Computer Networks<i>, vol. 146, no. 9, pp. 167–182,
    2018.
