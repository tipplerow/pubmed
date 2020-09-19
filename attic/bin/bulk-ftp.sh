#!/bin/sh
########################################################################
# Usage: bulk-ftp.sh
########################################################################

if [ -z "${PUBMED_HOME}" ]
then
    echo "Environment variable PUBMED_HOME is not set; exiting."
    exit 1
fi

${PUBMED_HOME}/bin/pubmed-run.sh pubmed.bulk.BulkFTP
