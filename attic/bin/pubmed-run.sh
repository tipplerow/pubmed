#!/bin/sh
########################################################################
# Usage: pubmed-run.sh [JVM OPTIONS] DRIVER_CLASS PROP_FILE1 [PROP_FILE2 ...]
########################################################################

if [ -z "${JAM_HOME}" ]
then
    echo "Environment variable JAM_HOME is not set; exiting."
    exit 1
fi

if [ -z "${PUBMED_HOME}" ]
then
    echo "Environment variable PUBMED_HOME is not set; exiting."
    exit 1
fi

if [ -z "${CORE_NLP_HOME}" ]
then
    echo "Environment variable CORE_NLP_HOME is not set; exiting."
    exit 1
fi

SCRIPT=`basename $0`
JAMRUN=${JAM_HOME}/bin/jam-run.sh

# -------------------------------------------------
# Extract any JVM flags beginning with a hyphen "-"
# -------------------------------------------------

JVM_FLAGS=""

while [[ "$1" == -* ]]
do
    JVM_FLAGS="${JVM_FLAGS} $1"
    shift
done

if [ $# -lt 1 ]
then
    echo "Usage: $SCRIPT [JVM OPTIONS] DRIVER_CLASS [PROP_FILE1 PROP_FILE2 ...]"
    exit 1
fi

DRIVER_CLASS=$1
shift

TIPPLEROW_CLASSPATH=${PUBMED_HOME}/lib/pubmed.jar

for CoreJar in ${CORE_NLP_HOME}/stanford-corenlp*.jar
do
    TIPPLEROW_CLASSPATH=${TIPPLEROW_CLASSPATH}:$CoreJar
done

export TIPPLEROW_CLASSPATH=$TIPPLEROW_CLASSPATH

$JAMRUN $SCAN_HOME $JVM_FLAGS $DRIVER_CLASS $@
