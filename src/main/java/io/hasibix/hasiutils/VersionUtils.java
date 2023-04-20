package io.hasibix.hasiutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VersionUtils {
    public class VersionFormatter{
        @Nonnull String format;
        @Nonnull Pattern pattern;
        @Nonnull List<String> fields;
        @Nullable Map<String, String> buildtypeList;

        public VersionFormatter(@Nonnull String format, @Nonnull Pattern pattern, @Nonnull List<String> fields, @Nullable Map<String, String> buildtypeList) {
            if(format.split(" ").length > 0) {
                this.format = format;
                this.pattern = pattern;
                this.fields = fields;
                this.buildtypeList = buildtypeList;
            }
        }

        public Version formatVersion(@Nonnull String merged) {
            Matcher matcher = this.pattern.matcher(merged);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid version format! Must be formatted properly.");
            }
            List<String> values = new ArrayList<String>();

            for (int i = 0; i < format.split(" ").length; i++) {
                if(this.fields.contains(format.split(" ")[i])) {
                    values.add(matcher.group(i));
                }
            }
            
            Map<String, String> splitted = new HashMap<String, String>();

            for (int i = 0; i < this.fields.size(); i++) {
                splitted.put(this.fields.get(i), values.get(i));
            }

            return new Version(merged, splitted, this.buildtypeList);
        }
    }

    public class Version {
        @Nonnull String merged;
        @Nonnull Map<String, String> splitted;
        @Nullable Map<String, String> buildtypeList;

        public Version(@Nonnull String merged, @Nonnull Map<String, String> splitted, @Nullable Map<String, String> buildtypeList) {
            this.merged = merged;
            this.splitted = splitted;
        }
    }
}
