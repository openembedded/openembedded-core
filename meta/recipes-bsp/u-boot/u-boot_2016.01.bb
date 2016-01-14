require u-boot.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2016.01"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "fa85e826c16b9ce1ad302a57e9c4b24db0d8b930"

PV = "v2016.01+git${SRCPV}"
