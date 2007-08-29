require linux-gta01.inc

SRC_URI += "svn://svn.openmoko.org/branches/src/target/kernel/2.6.21.x;module=patches;proto=http;rev=2832"
SRC_URI += "file://fix-EVIOCGRAB-semantics.patch;patch=1"

MOKOR = "moko11"
PR = "${MOKOR}-r2"

VANILLA_VERSION = "2.6.21.6"

