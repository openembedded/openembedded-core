require dtc.inc

LIC_FILES_CHKSUM = "file://GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
		    file://libfdt/libfdt.h;beginline=3;endline=52;md5=fb360963151f8ec2d6c06b055bcbb68c"

SRCREV = "033089f29099bdfd5c2d6986cdb9fd07b16cfde0"
PV = "1.3.0+git${SRCPV}"
PR = "${INC_PR}.1"

S = "${WORKDIR}/git"

BBCLASSEXTEND = "native nativesdk"
