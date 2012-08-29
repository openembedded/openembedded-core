python __anonymous() {
    bb.warn("%s: task.bbclass is deprecated, please inherit packagegroup instead" % d.getVar("PN", True))
}

inherit packagegroup

