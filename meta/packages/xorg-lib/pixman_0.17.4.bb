require xorg-lib-common.inc

DESCRIPTION = "Library for lowlevel pixel operations"
DEPENDS = "virtual/libx11"

PR="r1"

EXTRA_OECONF="--disable-gtk"
