DUMMYARCH = "sdk-provides-dummy-${SDKPKGSUFFIX}"

# Add /bin/sh?
DUMMYPROVIDES = "\
    /bin/bash \
    ${bindir}/env \
    pkgconfig \
    libGL.so()(64bit) \
    libGL.so \
"

require dummy-sdk-package.inc

inherit nativesdk
