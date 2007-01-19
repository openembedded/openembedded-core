PR = "r0"

SRC_URI += "file://iproute2-2.6.15_no_strip.diff;patch=1;pnum=0 \
            file://new-flex-fix.patch;patch=1"

require iproute2.inc

DATE = "061002"
