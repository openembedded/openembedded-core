require linux-gta01.inc

DEFAULT_PREFERENCE = "-6"

MOKOR = "moko10"
PR = "${MOKOR}-r0"
VANILLA_VERSION = "2.6.21.1"

SRC_URI += "svn://svn.openmoko.org/trunk/src/target/kernel;module=patches;proto=http"

