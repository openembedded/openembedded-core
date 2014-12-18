DEFAULTTUNE ?= "aarch64"

ARMPKGARCH ?= "aarch64"

TUNEVALID[aarch64] = "Enable instructions for aarch64"
TUNEVALID[bigendian] = "Enable big-endian mode."
TUNECONFLICTS[aarch64] = ""
MACHINEOVERRIDES .= "${@bb.utils.contains("TUNE_FEATURES", "aarch64", ":aarch64", "" ,d)}"

# Little Endian base configs
AVAILTUNES += "aarch64 aarch64_be"
TUNE_FEATURES_tune-aarch64 ?= "aarch64"
TUNE_FEATURES_tune-aarch64_be ?= "${TUNE_FEATURES_tune-aarch64} bigendian"

ARMPKGSFX_ENDIAN = "${@bb.utils.contains("TUNE_FEATURES", "bigendian", "_be", "", d)}"

TUNE_ARCH = "aarch64${ARMPKGSFX_ENDIAN}"
TUNE_PKGARCH = "aarch64${ARMPKGSFX_ENDIAN}"

PACKAGE_EXTRA_ARCHS = "aarch64${ARMPKGSFX_ENDIAN}"
