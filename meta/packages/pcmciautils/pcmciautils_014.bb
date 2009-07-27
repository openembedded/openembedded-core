require pcmciautils.inc

PR = "r2"

export LEX = "flex"

SRC_URI += "file://makefile_fix.patch;patch=1 \
            file://version_workaround.patch;patch=1 \
	    file://modalias_update.patch;patch=1"
