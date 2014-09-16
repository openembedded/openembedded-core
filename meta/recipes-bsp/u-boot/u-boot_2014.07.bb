require u-boot.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2014.07"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "524123a70761110c5cf3ccc5f52f6d4da071b959"

PV = "v2014.07+git${SRCPV}"
