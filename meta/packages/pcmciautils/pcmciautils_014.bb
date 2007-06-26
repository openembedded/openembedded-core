require pcmciautils.inc

PR = "r1"

SRC_URI += "file://makefile_fix.patch;patch=1 \
            file://version_workaround.patch;patch=1 \
	    file://modalias_update.patch;patch=1"
