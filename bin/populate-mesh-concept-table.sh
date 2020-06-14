#!/bin/sh
########################################################################
# Usage: populate-mesh-concept-table.sh PROD | TEST
########################################################################

if [ $# -ne 1 ]
then
    echo "Usage: `basename $0` PROD | TEST"
    exit 1
fi

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

${JAM_HOME}/bin/jam-run.sh ${PUBMED_HOME} pubmed.sql.MeshConceptTable $1


