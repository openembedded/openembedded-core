require bash.inc

PR = "r2"

SRC_URI = "${GNU_MIRROR}/bash/${BPN}-${PV}.tar.gz;name=tarball \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-001;apply=yes;striplevel=0;name=patch001 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-002;apply=yes;striplevel=0;name=patch002 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-003;apply=yes;striplevel=0;name=patch003 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-004;apply=yes;striplevel=0;name=patch004 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-005;apply=yes;striplevel=0;name=patch005 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-006;apply=yes;striplevel=0;name=patch006 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-007;apply=yes;striplevel=0;name=patch007 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-008;apply=yes;striplevel=0;name=patch008 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-009;apply=yes;striplevel=0;name=patch009 \
           ${GNU_MIRROR}/bash/bash-4.2-patches/bash42-010;apply=yes;striplevel=0;name=patch010 \
           file://execute_cmd.patch;striplevel=0 \
           "

SRC_URI[tarball.md5sum] = "3fb927c7c33022f1c327f14a81c0d4b0"
SRC_URI[tarball.sha256sum] = "a27a1179ec9c0830c65c6aa5d7dab60f7ce1a2a608618570f96bfa72e95ab3d8"
SRC_URI[patch001.md5sum] = "1100bc1dda2cdc06ac44d7e5d17864a3"
SRC_URI[patch001.sha256sum] = "8d6ca028576c4af23e660a2fbc2112221a11c8a785c0b37f033967e5cd12b47a"
SRC_URI[patch002.md5sum] = "30e7948079921d3261efcc6a40722135"
SRC_URI[patch002.sha256sum] = "febac927e199aceeba2004908d971d4afb49b521796c3f42d1166f9fbbfbcef9"
SRC_URI[patch003.md5sum] = "9ea06decec43a198f3d7cf29acc602f8"
SRC_URI[patch003.sha256sum] = "5a0a7c15018c87348ea87cb0beea14345faf878dbb0e25c17fa70677194cb4cd"
SRC_URI[patch004.md5sum] = "fb48f6134d7b013135929476aa0c250c"
SRC_URI[patch004.sha256sum] = "4e34b0f830d2583d56e14225a66937abc81f45bbafcd2eb49daf61c9462140c1"
SRC_URI[patch005.md5sum] = "e70e45de33426b38153b390be0dbbcd4"
SRC_URI[patch005.sha256sum] = "a81749e73004b81cfdf0fe075bec365dc1fef756ee5e3fd142821e317d1459a0"
SRC_URI[patch006.md5sum] = "ce4e5c484993705b27daa151eca242c2"
SRC_URI[patch006.sha256sum] = "c91148945a2ddafa792682d7c8668c59e7e645eae1334b15b0d5d9ad22634bd1"
SRC_URI[patch007.md5sum] = "88d1f96db29461767602e2546803bda7"
SRC_URI[patch007.sha256sum] = "405826acf443dd1084f236a15cb76d7f0ee2dbe5edff45c5fb836db571fb7e95"
SRC_URI[patch008.md5sum] = "24c574bf6d6a581e300823d9c1276af6"
SRC_URI[patch008.sha256sum] = "23080d11a60a78941210e2477f6bca066b45db03defa60da86fd765107ba2437"
SRC_URI[patch009.md5sum] = "4c5835f2fbab36c4292bb334977e5b6d"
SRC_URI[patch009.sha256sum] = "e7ed5440b4c19765786e90e4f1ded43195d38b3e4d1c4b39fcc23de9a74ccb20"
SRC_URI[patch010.md5sum] = "0a51602b535ef661ee707be6c8bdb373"
SRC_URI[patch010.sha256sum] = "acfc5482c25e6923116fcf4b4f7f6345b80f75ad7299749db4b736ad67aa43dc"

