# beecrypt-native OE build file
# Copyright (C) 2004-2005, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

require beecrypt_${PV}.bb
S = "${WORKDIR}/beecrypt-${PV}"

inherit native
EXTRA_OECONF="--enable-shared --enable-static"
