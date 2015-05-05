require bluez5.inc
SRC_URI[md5sum] = "24ba1d1e8e7ef5b8f4033a3059d7600e"
SRC_URI[sha256sum] = "1e3a4adb5a097dab83b3cf58e09c9541b815c6f33e3da972e18dfd1eb05d382c"

# noinst programs in Makefile.tools that are conditional on READLINE
# support
NOINST_TOOLS_READLINE ?= " \
    attrib/gatttool \
    tools/obex-client-tool \
    tools/obex-server-tool \
    tools/bluetooth-player \
    tools/obexctl \
"

# noinst programs in Makefile.tools that are conditional on EXPERIMENTAL
# support
NOINST_TOOLS_EXPERIMENTAL ?= " \
    emulator/btvirt \
    emulator/b1ee \
    emulator/hfp \
    tools/3dsp \
    tools/mgmt-tester \
    tools/gap-tester \
    tools/l2cap-tester \
    tools/sco-tester \
    tools/smp-tester \
    tools/hci-tester \
    tools/rfcomm-tester \
    tools/bdaddr \
    tools/avinfo \
    tools/avtest \
    tools/scotest \
    tools/amptest \
    tools/hwdb \
    tools/hcieventmask \
    tools/hcisecfilter \
    tools/btmgmt \
    tools/btinfo \
    tools/btattach \
    tools/btsnoop \
    tools/btproxy \
    tools/btiotest \
    tools/mcaptest \
    tools/cltest \
    tools/oobtest \
    tools/seq2bseq \
    tools/ibeacon \
    tools/btgatt-client \
    tools/btgatt-server \
    tools/gatt-service \
    profiles/iap/iapd \
"
