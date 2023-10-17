# Checks related to patch line lengths
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: GPL-2.0-only

import base
import pyparsing

class MaxLength(base.Base):
    add_mark = pyparsing.Regex('\+ ')
    max_length = 200

    def test_max_line_length(self):
        for patch in self.patchset:
            # for the moment, we are just interested in metadata
            if patch.path.endswith('.patch'):
                continue
            payload = str(patch)
            for line in payload.splitlines():
                if self.add_mark.search_string(line):
                    current_line_length = len(line[1:])
                    if current_line_length > self.max_length:
                        self.fail('Patch line too long (current length %s, maximum is %s)' % (current_line_length, self.max_length),
                                  data=[('Patch', patch.path), ('Line', '%s ...' % line[0:80])])
