#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2007, 2008, 2009, 2010 Red Hat, Inc.
#
# This copyrighted material is made available to anyone wishing to use, modify,
# copy, or redistribute it subject to the terms and conditions of the GNU
# General Public License v.2.  This program is distributed in the hope that it
# will be useful, but WITHOUT ANY WARRANTY expressed or implied, including the
# implied warranties of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, write to the Free Software Foundation, Inc., 51
# Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.  Any Red Hat
# trademarks that are incorporated in the source code or documentation are not
# subject to the GNU General Public License and may only be used or replicated
# with the express permission of Red Hat, Inc. 
#
from pykickstart.version import *
from pykickstart.commands import *

# This map is keyed on kickstart syntax version as provided by
# pykickstart.version.  Within each sub-dict is a mapping from command name
# to the class that handles it.  This is an onto mapping - that is, multiple
# command names can map to the same class.  However, the Handler will ensure
# that only one instance of each class ever exists.
commandMap = {
    FC3: {
        "bootloader": bootloader.FC3_Bootloader,
        "part": partition.FC3_Partition,
        "partition": partition.FC3_Partition,
    },

    # based on fc3
    FC4: {
        "bootloader": bootloader.FC4_Bootloader,
        "part": partition.FC4_Partition,
        "partition": partition.FC4_Partition,
    },

    # based on fc4
    FC5: {
        "bootloader": bootloader.FC4_Bootloader,
        "part": partition.FC4_Partition,
        "partition": partition.FC4_Partition,
    },

    # based on fc5
    FC6: {
        "bootloader": bootloader.FC4_Bootloader,
        "part": partition.FC4_Partition,
        "partition": partition.FC4_Partition,
    },

    # based on fc6
    F7: {
        "bootloader": bootloader.FC4_Bootloader,
        "part": partition.FC4_Partition,
        "partition": partition.FC4_Partition,
    },

    # based on f7
    F8: {
        "bootloader": bootloader.F8_Bootloader,
        "part": partition.FC4_Partition,
        "partition": partition.FC4_Partition,
    },

    # based on f8
    F9: {
        "bootloader": bootloader.F8_Bootloader,
        "part": partition.F9_Partition,
        "partition": partition.F9_Partition,
    },

    # based on f9
    F10: {
        "bootloader": bootloader.F8_Bootloader,
        "part": partition.F9_Partition,
        "partition": partition.F9_Partition,
    },

    # based on f10
    F11: {
        "bootloader": bootloader.F8_Bootloader,
        "part": partition.F11_Partition,
        "partition": partition.F11_Partition,
    },

    # based on f11
    F12: {
        "bootloader": bootloader.F12_Bootloader,
        "part": partition.F12_Partition,
        "partition": partition.F12_Partition,
    },

    # based on f12
    F13: {
        "bootloader": bootloader.F12_Bootloader,
        "part": partition.F12_Partition,
        "partition": partition.F12_Partition,
    },

    # based on f13
    F14: {
        "bootloader": bootloader.F14_Bootloader,
        "part": partition.F14_Partition,
        "partition": partition.F14_Partition,
    },

    # based on f14
    F15: {
        "bootloader": bootloader.F15_Bootloader,
        "part": partition.F14_Partition,
        "partition": partition.F14_Partition,
    },

    # based on f15
    F16: {
        "bootloader": bootloader.F15_Bootloader,
        "part": partition.F14_Partition,
        "partition": partition.F14_Partition,
    },

    # based on fc1
    RHEL3: {
        "bootloader": bootloader.FC3_Bootloader,
        "part": partition.FC3_Partition,
        "partition": partition.FC3_Partition,
    },

    # based on fc3
    RHEL4: {
        "bootloader": bootloader.FC3_Bootloader,
        "part": partition.FC3_Partition,
        "partition": partition.FC3_Partition,
    },

    # based on fc6
    RHEL5: {
        "bootloader": bootloader.RHEL5_Bootloader,
        "part": partition.RHEL5_Partition,
        "partition": partition.RHEL5_Partition,
    },

    # based on f13ish
    RHEL6: {
        "bootloader": bootloader.RHEL6_Bootloader,
        "part": partition.F12_Partition,
        "partition": partition.F12_Partition,
    }
}

# This map is keyed on kickstart syntax version as provided by
# pykickstart.version.  Within each sub-dict is a mapping from a data object
# name to the class that provides it.  This is a bijective mapping - that is,
# each name maps to exactly one data class and all data classes have a name.
# More than one instance of each class is allowed to exist, however.
dataMap = {
    FC3: {
        "PartData": partition.FC3_PartData,
    },
    FC4: {
        "PartData": partition.FC4_PartData,
    },
    FC5: {
        "PartData": partition.FC4_PartData,
    },
    FC6: {
        "PartData": partition.FC4_PartData,
    },
    F7: {
        "PartData": partition.FC4_PartData,
    },
    F8: {
        "PartData": partition.FC4_PartData,
    },
    F9: {
        "PartData": partition.F9_PartData,
    },
    F10: {
        "PartData": partition.F9_PartData,
    },
    F11: {
        "PartData": partition.F11_PartData,
    },
    F12: {
        "PartData": partition.F12_PartData,
    },
    F13: {
        "PartData": partition.F12_PartData,
    },
    F14: {
        "PartData": partition.F14_PartData,
    },
    F15: {
        "PartData": partition.F14_PartData,
    },
    F16: {
        "PartData": partition.F14_PartData,
    },
    RHEL3: {
        "PartData": partition.FC3_PartData,
    },
    RHEL4: {
        "PartData": partition.FC3_PartData,
    },
    RHEL5: {
        "PartData": partition.RHEL5_PartData,
    },
    RHEL6: {
        "PartData": partition.F12_PartData,
    }
}
