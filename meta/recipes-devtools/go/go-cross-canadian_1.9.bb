require go-cross-canadian.inc
require go-${PV}.inc

export GOHOSTOS_CROSS = "${HOST_GOOS}"
export GOHOSTARCH_CROSS = "${HOST_GOARCH}"
export CC_FOR_TARGET = "${HOST_PREFIX}gcc --sysroot=${STAGING_DIR_HOST}${SDKPATHNATIVE}"
export CXX_FOR_TARGET = "${HOST_PREFIX}g++ --sysroot=${STAGING_DIR_HOST}${SDKPATHNATIVE}"

do_compile_prepend() {
	export GOBIN="${B}/bin"
	export TMPDIR="$GOTMPDIR"
}
