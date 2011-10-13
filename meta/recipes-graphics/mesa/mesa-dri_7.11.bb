include mesa-common.inc
include mesa-${PV}.inc
include mesa-dri.inc
PR = "${INC_PR}.0"

EXTRA_OECONF += "--with-dri-drivers=swrast,i915,i965"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'
