
-- Enums
CREATE TYPE material_type AS ENUM ('pdf', 'video', 'link', 'other');

-- Materials metadata table
CREATE TABLE material (
    id UUID PRIMARY KEY,
    course_id UUID NOT NULL,
    creator_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    material_type material_type NOT NULL,
    firebase_path VARCHAR(255) NOT NULL,
    size INTEGER NOT NULL,
    upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_published BOOLEAN NOT NULL DEFAULT false
);

-- Indexes
CREATE INDEX idx_material_course ON material(course_id);
CREATE INDEX idx_material_creator ON material(creator_id);
CREATE INDEX idx_material_type ON material(material_type);