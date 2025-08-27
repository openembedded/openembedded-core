require readline.inc

SRC_URI += "file://norpath.patch \
            file://fix-for-readline-event-hook.patch \
            "

SRC_URI[archive.sha256sum] = "fe5383204467828cd495ee8d1d3c037a7eba1389c22bc6a041f627976f9061cc"
