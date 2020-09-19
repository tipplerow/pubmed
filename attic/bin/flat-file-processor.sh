#!/bin/sh
########################################################################
# Usage: flat-file-processor.sh DIR1 [DIR2 ...]
########################################################################

if [ $# -lt 1 ]
then
    echo "Usage: `basename $0` DIR1 [DIR2 ...]"
    exit 1
fi

if [ -z "${PUBMED_HOME}" ]
then
    echo "Environment variable PUBMED_HOME is not set; exiting."
    exit 1
fi

${PUBMED_HOME}/bin/pubmed-run.sh pubmed.flat.FlatFileProcessor $@
