require u-boot.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2015.01"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "92fa7f53f1f3f03296f8ffb14bdf1baefab83368"

PV = "v2015.01+git${SRCPV}"
